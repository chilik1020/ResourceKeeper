package com.chilik1020.resourcekeeper.controller.temperature

import com.chilik1020.resourcekeeper.controller.temperature.listener.BaseTemperatureLimitTracker
import com.chilik1020.resourcekeeper.model.DataBuffer
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.chilik1020.resourcekeeper.model.log.CommonLogWriter
import com.chilik1020.resourcekeeper.model.data.TemperaturePoint
import com.chilik1020.resourcekeeper.model.log.TemperatureLogWriter
import com.chilik1020.resourcekeeper.model.temperature.FakeTemperatureDevice
import com.chilik1020.resourcekeeper.model.temperature.RS232TemperatureDevice

import com.chilik1020.resourcekeeper.ui.JPanelTemperature
import com.chilik1020.resourcekeeper.utils.JsonConfig
import javax.inject.Inject
import javax.inject.Named

class OwenTemperatureController : BaseTemperatureController() {


    @Inject lateinit var viewTemperatureMain: JPanelTemperature
    @field:[Inject Named("owen")] lateinit var buffer: DataBuffer<TemperaturePoint>
    @Inject lateinit var sourceDevice: FakeTemperatureDevice
    @Inject lateinit var commonLogWriter: CommonLogWriter
    @Inject lateinit var temperatureLogWriter: TemperatureLogWriter


    private val sourceList = mutableListOf<String>()

    init {
        view = viewTemperatureMain.panelOwenCharts
        for (i in 0 until JsonConfig.tempChanelsOwen.size) {
            currentTemp.add(0.0)
        }

        view.addListeners(this, BaseTemperatureLimitTracker(view, this))

        GlobalScope.launch {
            while (true) {
                delay(1000)
                val list = getAvailablePorts()
                if (!sourceList.containsAll(list) && sourceList.size != list.size) {
                    view.sourceChoose.removeAllItems()
                    sourceList.clear()
                    for (e in list) {
                        sourceList.add(e)
                        view.sourceChoose.addItem(e)
                    }
                }
            }
        }
    }

    override fun consume(t: TemperaturePoint) {
        commonLogWriter.temperatureOwen = t
        super.consume(t)
    }

    override fun startReading() {
        setPortName(view.sourceChoose.selectedItem as String)
        sourceDevice.setReadEn(true)

        GlobalScope.launch {
            sourceDevice.startReading()
        }
    }

    override fun stopReading() {
        sourceDevice.setReadEn(false)
    }

    private fun setPortName(port: String) {
        sourceDevice.setSource(port)
    }

    private fun getAvailablePorts():List<String> {
        return sourceDevice.getAvailable().toList()
    }

    override fun getDataBuffer(): DataBuffer<TemperaturePoint> {
        return buffer
    }

}