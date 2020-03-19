package com.chilik1020.resourcekeeper.model.log

import com.chilik1020.resourcekeeper.model.DataBuffer
import com.chilik1020.resourcekeeper.model.data.EnergyCycle
import java.math.BigDecimal

class CommonLogReader(buffer: DataBuffer<EnergyCycle>): LogReader<EnergyCycle>(buffer) {

    override fun parseLineAndProduce(line: String) {
        try {
            val splited = line.split(",")
            if (splited.size < 6)
                return

            val date = splited[0] + splited[1]
            val currentShot = Integer.parseInt(splited[2].trim()).toLong()
            val fullResource = Integer.parseInt(splited[3].trim()).toLong()
            val startEnergy = BigDecimal(splited[4].trim())
            val stopEnergy = BigDecimal(splited[5].trim())
            val meanEnergy = BigDecimal(splited[6].trim())
            val ec = EnergyCycle(date,currentShot,fullResource, startEnergy.toDouble(), stopEnergy.toDouble(), meanEnergy.toDouble())
            produce(ec)
        }
        catch (ex: IllegalArgumentException) {
            println("CommonLogReader: $ex")
        }
    }

}