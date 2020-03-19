package com.chilik1020.resourcekeeper.model.log

import com.chilik1020.resourcekeeper.model.DataBuffer
import com.chilik1020.resourcekeeper.model.data.EnergyCycle
import com.chilik1020.resourcekeeper.model.data.TemperaturePoint
import com.chilik1020.resourcekeeper.utils.JsonConfig
import java.io.File
import java.io.RandomAccessFile
import java.lang.StringBuilder
import java.util.*

class CommonLogWriter(val buffer: DataBuffer<EnergyCycle>){

    var temperatureOwen: TemperaturePoint = TemperaturePoint(Date(), listOf(0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0))
    var temperatureLtuInner: TemperaturePoint = TemperaturePoint(Date(), listOf(0.0,0.0,0.0))

    lateinit var file: File
    var currentResource = 0L

    private val sb = StringBuilder()

    fun writeCycleInLog(ec: EnergyCycle) {
        val raf = RandomAccessFile(file, "rw")
        raf.seek(file.length())

        currentResource += JsonConfig.shotsInCycle
        ec.fullResource = currentResource

        energyCycleToStringBuilder(ec)
        temperatureToStringBuilder()

        raf.writeBytes(sb.toString())
        raf.close()

        sb.clear()
        buffer.add(ec)
    }

    private fun temperatureToStringBuilder() {
        temperatureOwen.temperature.forEach { sb.append(", $it") }
        temperatureLtuInner.temperature.forEach { sb.append(", $it") }
        sb.append("\n")
    }

    private fun energyCycleToStringBuilder(ec: EnergyCycle) {
        sb.append(ec.date)
        sb.append(", ")
        sb.append(ec.currentShot)
        sb.append(", ")
        sb.append(ec.fullResource)
        sb.append(", ")
        sb.append(ec.startEnergy)
        sb.append(", ")
        sb.append(ec.stopEnergy.toString())
        sb.append(", ")
        sb.append(ec.meanEnergy.toString())
    }
}