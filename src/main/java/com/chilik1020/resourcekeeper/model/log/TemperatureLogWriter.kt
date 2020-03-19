package com.chilik1020.resourcekeeper.model.log

import com.chilik1020.resourcekeeper.base.Consumer
import com.chilik1020.resourcekeeper.model.data.TemperaturePoint
import com.chilik1020.resourcekeeper.utils.JsonConfig
import com.chilik1020.resourcekeeper.utils.formatDateTime
import com.chilik1020.resourcekeeper.utils.getDate
import java.io.File
import java.io.RandomAccessFile
import java.lang.StringBuilder

class TemperatureLogWriter: Consumer<TemperaturePoint> {

    private lateinit var path: String
    private lateinit var file: File
    private lateinit var raf: RandomAccessFile

    override fun consume(t: TemperaturePoint) {
        raf.seek(file.length())
        raf.writeBytes(temperaturePointToLogFormat(t))
    }

    private fun temperaturePointToLogFormat(t: TemperaturePoint): String{
        val sb = StringBuilder()
        for (temp in t.temperature) {
            sb.append(temp)
            sb.append(",")
        }
        sb.append(formatDateTime(t.date))
        sb.append("\n")
        return sb.toString()
    }

    override fun onDestroyConsumer() {
    }

    fun setSource() {
        val date = getDate()
        path = JsonConfig.tempLogDefaultPath

        val temperatureLogDir = File(path)
        if (!temperatureLogDir.exists())
            temperatureLogDir.mkdirs()

        file = File("$path\\temperature_$date.txt")
        raf = RandomAccessFile(file, "rw")
    }

}