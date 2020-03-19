package com.chilik1020.resourcekeeper.model

import com.chilik1020.resourcekeeper.base.Consumer
import com.chilik1020.resourcekeeper.utils.JsonConfig
import com.chilik1020.resourcekeeper.model.data.EnergyCycle
import com.chilik1020.resourcekeeper.model.data.EnergyShot
import com.chilik1020.resourcekeeper.model.log.CommonLogWriter
import com.chilik1020.resourcekeeper.utils.getDateTime
import java.math.BigDecimal
import java.math.RoundingMode

class EnergyProcess(private val logWriter: CommonLogWriter) : Consumer<EnergyShot> {

    private var listCycle = mutableListOf<EnergyShot>()
    private var currentShot = 0L

    override fun consume(t: EnergyShot) {
        if (t.count == 0L)
            return

        listCycle.add(t)

        if (t.count % JsonConfig.shotsInCycle == 0L) {
            currentShot = t.count
            processCycle()
            listCycle.clear()
        }
    }

    private fun processCycle() {
        val list = mutableListOf<EnergyShot>()
        list.addAll(listCycle)

        if (list.size < 10)
            return

        val startEnergy = calcMeanValue(list.subList(0,10))
        val meanCycleEnergy = calcMeanValue(list)
        val stopEnergy = calcMeanValue(list.subList(list.size-10,list.size))


        val ec = EnergyCycle(getDateTime(),currentShot,0,startEnergy, stopEnergy, meanCycleEnergy)
        produce(ec)
    }

    private fun produce(ec: EnergyCycle) {
        logWriter.writeCycleInLog(ec)
    }

    private fun calcMeanValue(list: MutableList<EnergyShot>): Double {
        var result = 0.0
        for (ep in list)
            result += ep.energy

        result = result/list.size * 1000
        result = BigDecimal(result).setScale(2, RoundingMode.HALF_EVEN).toDouble()

        return result
    }

    override fun onDestroyConsumer() {
        listCycle.clear()
    }
}