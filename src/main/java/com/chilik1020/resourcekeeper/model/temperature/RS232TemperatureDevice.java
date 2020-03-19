package com.chilik1020.resourcekeeper.model.temperature;

import com.chilik1020.resourcekeeper.base.Producer;
import com.chilik1020.resourcekeeper.model.data.TemperaturePoint;
import com.chilik1020.resourcekeeper.utils.CRCUtilKt;
import com.chilik1020.resourcekeeper.utils.JsonConfig;
import jssc.*;
import com.chilik1020.resourcekeeper.model.DataBuffer;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class RS232TemperatureDevice implements Producer<TemperaturePoint> {

    private DataBuffer<TemperaturePoint> buffer;

    public void setBuffer(DataBuffer<TemperaturePoint> buffer) {
        this.buffer = buffer;
    }

    public DataBuffer<TemperaturePoint> getBuffer() {
        return buffer;
    }

    private boolean readEn = false;
    private boolean readError = false;

    private SerialPort serialPort;
    private String portName;

    private final long READ_FREQ = JsonConfig.tempReadingPeriod *1000;

    private boolean broadcastFlag = false;
    private boolean confirmationFlag = false;
    private final byte  BROADCAST = 0x71;
    private final byte  READ16BYTES = 0x03;
    private final byte  ADDR = (byte)0xA0;

    private Integer[] data = new Integer[19];
    private List<Double> dataConversionList = new ArrayList<>();
    private byte index = 0;


    @Inject
    public RS232TemperatureDevice(DataBuffer<TemperaturePoint> buffer) {
        this.buffer = buffer;
    }

    void open() {
        try {
            serialPort.openPort();
            serialPort.setParams(SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_2,
                    SerialPort.PARITY_EVEN);
            //Выключаем аппаратное управление потоком
            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
            //Устанавливаем ивент лисенер и маску
            int mask = SerialPort.MASK_RXCHAR;
            serialPort.addEventListener(new PortReader(), mask);

        } catch (SerialPortException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public void close() {
        try {
            if (serialPort != null && serialPort.isOpened()) {
                serialPort.closePort();
                serialPort = null;
            }
        } catch (SerialPortException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    void connectToAC2() throws SerialPortException, InterruptedException {
        serialPort.setRTS(false);
        serialPort.setDTR(true);
        serialPort.setRTS(true);
        Thread.sleep(10);
        serialPort.setRTS(false);

    }

    public void startReading(){
        try {
            if (serialPort == null)
                serialPort = new SerialPort(portName);

            if (!serialPort.isOpened())
                open();

            connectToAC2();

            while (readEn) {
                reading();
            }

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        } finally {
            close();
        }
    }

    private void reading() throws SerialPortException, InterruptedException, IOException {

        readOwen16Bytes(ADDR);

        if (!readError) {
            Thread.sleep(1000);
            if(CRCUtilKt.checkCRC(Arrays.copyOfRange(data,2,18), data[18]))
                produceTemperaturePoint();

            Thread.sleep(READ_FREQ);
        } else {
            System.out.println("WARNING: Failure reading " + portName);
            Thread.sleep(1000);
            setConfirmationFlag(false);
            return;
        }

        setConfirmationFlag(false);
    }

    private void readOwen16Bytes(byte address) throws SerialPortException {
        readError = false;
        int delayCount = 1000000;
        setBroadcastFlag(true);
        serialPort.writeByte(BROADCAST);
        while(!isConfirmationFlag()){
            delayCount--;
            if (delayCount == 0) {
                readError = true;
                return;
            }
        }
        serialPort.writeByte(READ16BYTES);
        serialPort.writeByte(address);
    }

    private void produceTemperaturePoint() {
        dataConversionList.clear();
        for (int i = 0; i < 8; i++) {
            dataConversionList.add(getDoubleValue(data[(i+1)*2 + 1],data[(i+1)*2]));
        }
        List<Double> list = new ArrayList<>(dataConversionList);
        TemperaturePoint tp = new TemperaturePoint(new Date(), list);
        produce(tp);
    }

    private double getDoubleValue(int ms, int ls) {
        double result;

        if (ms == 170)
            return 0.0;

        if (ms == 255)                              //shlom41k_28.07.2019
            result = (ms*255 + ls) - 65535 + 255;   //shlom41k
        else if (ms > 0)
            result = ms*255 + ls;
        else
            result = ls;

        return result/10.0;
    }

    @Override
    public void setReadEn(boolean readEn) {
        this.readEn = readEn;
    }

    public synchronized boolean isBroadcastFlag() {
        return broadcastFlag;
    }

    public synchronized void setBroadcastFlag(boolean broadcastFlag) {
        this.broadcastFlag = broadcastFlag;
    }

    public synchronized boolean isConfirmationFlag() {
        return confirmationFlag;
    }

    public synchronized void setConfirmationFlag(boolean confirmationFlag) {
        this.confirmationFlag = confirmationFlag;
    }

    @Override
    public void setSource(@NotNull String source) {
        this.portName = source;
    }

    @NotNull
    @Override
    public String[] getAvailable() {
        return SerialPortList.getPortNames();
    }

    private class PortReader implements SerialPortEventListener {
        @Override
        public void serialEvent(SerialPortEvent event) {
            if(event.isRXCHAR()){
                if(event.getEventValue() > 0){
                    try {
                        byte[] buffer = serialPort.readBytes();
                        if (buffer != null && buffer.length != 0) {
                            if (confirmationFlag) {
                                data[index] = Byte.toUnsignedInt(buffer[0]);
                                index++;
                            } else
                                index = 0;

                            if (isBroadcastFlag() && buffer[0] == 0x55) {
                                setConfirmationFlag(true);
                                setBroadcastFlag(false);
                            }
                        }
                    }
                    catch (Exception ex) {
                        System.out.println("ERROR: Serial port event");
                    }
                }
            }
        }
    }



    @Override
    public void produce(TemperaturePoint tp) {
        buffer.add(tp);
    }

//    public static String bytesToHex(byte[] bytes) {
//        final char[] hexArray = "0123456789ABCDEF".toCharArray();
//        char[] hexChars = new char[bytes.length * 2];
//        for ( int j = 0; j < bytes.length; j++ ) {
//            int v = bytes[j] & 0xFF;
//            hexChars[j * 2] = hexArray[v >>> 4];
//            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
//        }
//        return new String(hexChars);
//    }
}
