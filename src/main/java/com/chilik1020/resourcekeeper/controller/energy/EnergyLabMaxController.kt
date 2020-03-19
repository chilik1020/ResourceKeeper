package com.chilik1020.resourcekeeper.controller.energy

import com.chilik1020.resourcekeeper.base.BaseController
import com.chilik1020.resourcekeeper.base.Consumer
import com.chilik1020.resourcekeeper.model.AlertSystem
import com.chilik1020.resourcekeeper.model.DataBuffer
import com.chilik1020.resourcekeeper.model.EnergyProcess
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.chilik1020.resourcekeeper.model.data.EnergyShot
import com.chilik1020.resourcekeeper.model.log.LabMaxLogReader
import org.jfree.data.Range
import com.chilik1020.resourcekeeper.ui.JPanelEnergy
import java.text.DecimalFormat
import javax.inject.Inject

class EnergyLabMaxController: BaseController(), Consumer<EnergyShot> {

    @Inject lateinit var view: JPanelEnergy
    @Inject lateinit var buffer: DataBuffer<EnergyShot>
    @Inject lateinit var model: LabMaxLogReader
    @Inject lateinit var energyProcess: EnergyProcess
    @Inject lateinit var alertSystem: AlertSystem

    val consumersBufferLabMax = mutableListOf<Consumer<EnergyShot>>()

    var filePath = ""

    var shots = mutableListOf<Double>()
    var currentEnergy: Double = 0.0
    var minEnergy: Double = 0.0
    var maxEnergy: Double = 0.0
    var meanEnergy: Double = 0.0

    private var count = 10000L

    private var rangeDomain = 10000L
    private var rangeString = ""

    init {
        view.addListeners(this)
    }


    fun startReading() {
        model.readEn = true
        GlobalScope.launch {
            model.startReading()
        }
    }

    fun stopReading() {
        model.readEn = false
        model.logPath = ""
        buffer.clear()
        resetView()
    }

    override fun consume(t: EnergyShot) {
        currentEnergy = t.energy
        count = t.count

        val energy = currentEnergy * 1000

        shots.add(energy)

       /* Add new point to XYSeries */
        view.seriesLabMax.add(count, energy)

        updateDomainRange()
        updateEnergyValues()

        view.energyCurrent.text = DecimalFormat("#0.00").format(energy).replace(',', '.')

        view.shotCurrent.text = count.toString()


        /*  Scale Y axis */
        view.plotLabMax.rangeAxis.range = Range(minEnergy-0.1, maxEnergy+0.1)

    }

    private fun updateEnergyValues() {
        if (shots.isEmpty())
            return

        if (shots.size < rangeDomain)
            calcEnergyValues(shots)
        else
            calcEnergyValues(shots.subList((shots.size-rangeDomain).toInt(),shots.size))

        view.energyMin.text = DecimalFormat("#0.00").format(minEnergy).replace(',', '.')
        view.energyMax.text = DecimalFormat("#0.00").format(maxEnergy).replace(',', '.')
        view.energyMean.text = DecimalFormat("#0.00").format(meanEnergy).replace(',', '.')
    }

    private fun calcEnergyValues(shots: List<Double>){
        var minValueTemp = shots[0]
        var maxValueTemp = shots[0]
        var summ = 0.0

        for (s in shots) {
            if (s < minValueTemp)
                minValueTemp = s

            if (s > maxValueTemp)
                maxValueTemp = s

            summ += s
        }

        minEnergy = minValueTemp
        maxEnergy = maxValueTemp
        meanEnergy = summ / shots.size
    }

    private fun updateDomainRange() {
        if (rangeString == "Все" && count != 0L)
            rangeDomain = count

        when {
            count == 0L -> view.plotLabMax.domainAxis.range = Range(0.0, rangeDomain.toDouble())
            count < rangeDomain -> view.plotLabMax.domainAxis.range = Range(0.0, rangeDomain.toDouble())
            count == rangeDomain -> view.plotLabMax.domainAxis.range = Range(0.0, count.toDouble())
            else -> view.plotLabMax.domainAxis.range = Range(count-rangeDomain.toDouble(), count.toDouble())
        }

        updateEnergyValues()
    }

    fun setDomainRange(range: String) {
        rangeString = range
        rangeDomain = if (range == "Все") count+1 else range.toLong()
        updateDomainRange()
    }

    override fun onDestroyConsumer() {
    }

    private fun resetView(){
        count = 0L
        view.seriesLabMax.clear()
    }

    fun setLogFile(path: String) {
        model.logPath = path
        filePath = path
    }

}