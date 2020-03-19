package com.chilik1020.resourcekeeper.controller.energy

import com.chilik1020.resourcekeeper.base.BaseController
import com.chilik1020.resourcekeeper.base.Consumer
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.chilik1020.resourcekeeper.model.DataBuffer
import com.chilik1020.resourcekeeper.model.data.EnergyCycle
import com.chilik1020.resourcekeeper.model.log.CommonLogReader
import com.chilik1020.resourcekeeper.model.log.CommonLogWriter
import org.jfree.data.Range
import com.chilik1020.resourcekeeper.ui.JPanelEnergy
import java.lang.Exception
import javax.inject.Inject
import javax.swing.JOptionPane


class EnergyCommonLogController: BaseController(), Consumer<EnergyCycle> {

    @Inject lateinit var view: JPanelEnergy
    @Inject lateinit var buffer: DataBuffer<EnergyCycle>
    @Inject lateinit var model: CommonLogReader
    @Inject lateinit var resourceLogWriter: CommonLogWriter

    val consumersBufferResource = mutableListOf<Consumer<EnergyCycle>>()

    private var minValueY: Double = 0.0
    private var maxValueY: Double = 0.1

    var fullResource = 0L
    private var rangeDomain = 100000L
    private var rangeString = ""

    init {
        view.addListeners(this)
    }

    fun start() {
        model.readEn = true
        GlobalScope.launch {
            model.startReading()
        }
    }

    override fun consume(t: EnergyCycle) {

        fullResource = t.fullResource
        val startEnergy = t.startEnergy
        val stopEnergy = t.stopEnergy
        val meanEnergy = t.meanEnergy


//        if (minValueY == 0.0 || meanEnergy < minValueY)
//            minValueY = meanEnergy - 5
//
//        if (maxValueY == 0.0 || meanEnergy > maxValueY)
//            maxValueY = meanEnergy + 5


        updateDomainRange()

        /*  Scale Y axis */
//        view.plotCommon.rangeAxis.range = Range(minValueY, maxValueY)

        /* Add new point to XYSeries */
        view.seriesCommonStartEnergy.add(fullResource, startEnergy)
        view.seriesCommonStopEnergy.add(fullResource, stopEnergy)
        view.seriesCommonMeanEnergy.add(fullResource, meanEnergy)
        view.resourceValue.text = fullResource.toString()

    }

    private fun updateDomainRange() {
        if (rangeString == "Все" && fullResource != 0L)
            rangeDomain = fullResource

        when {
            fullResource == 0L -> view.plotCommon.domainAxis.range = Range(0.0, rangeDomain.toDouble()+1)
            fullResource < rangeDomain -> view.plotCommon.domainAxis.range = Range(0.0, rangeDomain.toDouble()+1)
            fullResource == rangeDomain -> view.plotCommon.domainAxis.range = Range(0.0, fullResource.toDouble()+1)
            else -> view.plotCommon.domainAxis.range = Range(fullResource-rangeDomain.toDouble(), fullResource.toDouble()+1)
        }
    }

    fun setDomainRange(range: String) {
        rangeString = range
        rangeDomain = if (range == "Все") fullResource else range.toLong()
        updateDomainRange()
    }


    fun setAxisRange() {
        try {
            minValueY = view.minYCommonChart.text.toDouble()
            maxValueY = view.maxYCommonChart.text.toDouble()
            view.plotCommon.rangeAxis.range = Range(minValueY, maxValueY)
        } catch (ex: Exception){
            view.createAlert("Ошибка", "Некорректное значение", JOptionPane.ERROR_MESSAGE)
        }
    }

    override fun onDestroyConsumer() {}

    fun stop() {
        model.readEn = false
        model.logPath = ""
    }

    fun setLogFile(path: String) {
        model.logPath = path
    }
}