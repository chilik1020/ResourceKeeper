package com.chilik1020.resourcekeeper.controller.temperature

import com.chilik1020.resourcekeeper.base.BaseController
import com.chilik1020.resourcekeeper.base.Consumer
import com.chilik1020.resourcekeeper.model.AlertSystem
import com.chilik1020.resourcekeeper.model.DataBuffer
import com.chilik1020.resourcekeeper.model.data.TemperaturePoint
import com.chilik1020.resourcekeeper.ui.JPanelBaseTemperatureChartsPanel
import org.jfree.data.Range
import org.jfree.data.time.Second
import javax.inject.Inject


abstract class BaseTemperatureController: BaseController(), Consumer<TemperaturePoint> {

    lateinit var view: JPanelBaseTemperatureChartsPanel

    @Inject
    lateinit var alertSystem: AlertSystem
//    @Inject lateinit var buffer: DataBuffer<OwenTemperaturePoint>

    var tempPoints = mutableListOf<TemperaturePoint>()
    var currentTemp = mutableListOf<Double>()

    var startTime = 0L
    var currentTime = 0L
    var fullTimeRange = 0L

    var rangeDomain = 10000.0
    var rangeString = ""

    abstract fun startReading()
    abstract fun stopReading()

    override fun consume(t: TemperaturePoint) {
        val temperatureArray = t.temperature
        val date = t.date
        currentTime = date.time

        if(startTime == 0L)
            startTime = currentTime

        tempPoints.add(t)

        for (i in 0 until view.tempChTrackEns.size) {
            val temp = temperatureArray[i]
            currentTemp[i] = temp
            view.tempChCurrentsOnChart[i].text = temp.toString()
            view.listSeries[i].addOrUpdate(Second(date),temp)
        }

        updateRanges()
    }

    override fun onDestroyConsumer() {
        stopReading()
    }


    open fun updateRanges() {
        updateDomainRange()
        updateAxisRange()
    }

    open fun updateDomainRange() {
        if (rangeString == "Всё время")
            rangeDomain = currentTime.toDouble()

        fullTimeRange = currentTime - startTime

        when {
            rangeDomain >= fullTimeRange ->{
                for (plot in view.listPlots)
                    plot.domainAxis.range = Range(startTime.toDouble(), currentTime.toDouble()+1)
            }
            else -> {
                for (plot in view.listPlots)
                    plot.domainAxis.range = Range(currentTime-rangeDomain, currentTime.toDouble())
            }
        }
    }

    open fun updateAxisRange() {
        when {
            rangeDomain >= fullTimeRange -> {
                calcRanges(tempPoints)
            }
            else -> {
                var firstPointIndex = 0

                for (i in 0 until tempPoints.size) {
                    if (tempPoints[i].date.time >= currentTime - rangeDomain) {
                        firstPointIndex = i
                        break
                    }
                }

                calcRanges(tempPoints.subList(firstPointIndex, tempPoints.size))
            }
        }
    }

    open fun calcRanges(points: List<TemperaturePoint>) {
        if (points.isEmpty())
            return

        var listMins = mutableListOf<Double>()
        var listMaxs = mutableListOf<Double>()

        for (i in 0 until view.tempChTrackEns.size) {
            listMins.add(points[0].temperature[i]-0.1)
            listMaxs.add(points[0].temperature[i] +0.1)
        }

        for(p in points) {
            val tempList = p.temperature

            for (i in 0 until view.tempChTrackEns.size) {
                val temp = tempList[i]
                if (temp >= listMaxs[i]) {
                    listMaxs[i] = temp + 0.1
                }

                if (temp <= listMins[i])
                    listMins[i] = temp - 0.1

            }
        }
        for (i in 0 until view.tempChTrackEns.size) {
            view.listPlots[i].rangeAxis.range = Range(listMins[i], listMaxs[i])
        }
    }


    open fun setDomainRange(range: String) {
        rangeString = range
        when (range) {
            "10 мин" -> rangeDomain = 10*60*1000.0
            "30 мин" -> rangeDomain = 30*60*1000.0
            "1 час" -> rangeDomain = 60*60*1000.0
            "2 часа" -> rangeDomain = 120*60*1000.0
            "5 часов" -> rangeDomain = 300*60*1000.0
            "Всё время" -> rangeDomain = (currentTime).toDouble()
        }
        updateRanges()
    }

    open fun resetView(){
        startTime = 0L

        view.listSeries.forEach { it.clear() }
        view.tempChCurrentsOnChart.forEach { it.text = "      " }

        tempPoints.clear()
    }

    abstract fun getDataBuffer(): DataBuffer<TemperaturePoint>
}