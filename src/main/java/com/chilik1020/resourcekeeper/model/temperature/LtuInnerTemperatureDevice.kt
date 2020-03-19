package com.chilik1020.resourcekeeper.model.temperature

import com.chilik1020.resourcekeeper.base.Producer
import com.chilik1020.resourcekeeper.model.DataBuffer
import com.chilik1020.resourcekeeper.model.data.EnergyCycle
import com.chilik1020.resourcekeeper.model.data.TemperaturePoint
import com.chilik1020.resourcekeeper.utils.JsonConfig
import java.io.File
import java.io.RandomAccessFile
import java.lang.Exception
import java.math.BigDecimal
import java.util.*

class LtuInnerTemperatureDevice (private val buffer: DataBuffer<TemperaturePoint>): Producer<TemperaturePoint> {

    private lateinit var file: File
    private  var logPath: String = JsonConfig.tempInnerSensorsLogPath
    private var readEn: Boolean = false

    private var lastReadLine: String = ""

    private val FREQ1 = 300L
    private val FREQ2 = JsonConfig.periodShotMs
    private var readFreqMs: Long = 0L

    override fun startReading() {
        try {
            file = File(logPath)

            val raf = RandomAccessFile(file,"r")
            val currentPosition = raf.length()

//        lines.forEach {
//            parseLineAndProduce(it)
//        }
            raf.seek(currentPosition)

            readFreqMs = FREQ1

            while (readEn) {

                when (val line = raf.readLine()) {
                    lastReadLine -> {}
                    null -> readFreqMs = FREQ2
                    else -> {
                        lastReadLine = line
                        parseLineAndProduce(line)
                    }
                }

                Thread.sleep(readFreqMs)
            }
        } catch (ex: Exception) {
            println("Error: File tmp.dat not found")
        }

    }

    override fun setSource(source: String) {
    }

    override fun getAvailable() = arrayOf("tmp.dat")

    override fun setReadEn(state: Boolean) {
        readEn = state
    }

    override fun produce(t: TemperaturePoint) {
        buffer.add(t)
    }

    private fun parseLineAndProduce(line: String) {
        try {
            if (line.length > 48) {
                val tempLine = line.substring(line.length-17)
                val tempArray = tempLine.split(" ").map { it.toDouble() }
                produce(TemperaturePoint(Date(), tempArray.toList()))
            }
        }
        catch (ex: IllegalArgumentException) {
            println("LtuInner parse line error: $ex")
        }
    }

}