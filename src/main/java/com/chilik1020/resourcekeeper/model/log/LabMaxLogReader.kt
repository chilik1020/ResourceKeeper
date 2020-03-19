package com.chilik1020.resourcekeeper.model.log

import com.chilik1020.resourcekeeper.model.DataBuffer
import com.chilik1020.resourcekeeper.model.data.EnergyShot
import java.math.BigDecimal

class LabMaxLogReader(buffer: DataBuffer<EnergyShot>): LogReader<EnergyShot>(buffer) {

    override fun parseLineAndProduce(line: String) {
        try {
            val splited = line.split(",")
            if (splited.size <3)
                return

            val count = Integer.parseInt(splited[0]).toLong()
            val bd = BigDecimal(splited[1].replace(',','.'))
            produce(EnergyShot(count, bd.toDouble()))
        } catch (ex: Exception) {
//            println("LabMaxLogReader: $ex")
        }
    }
}