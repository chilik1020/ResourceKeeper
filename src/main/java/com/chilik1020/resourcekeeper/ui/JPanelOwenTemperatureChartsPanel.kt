package com.chilik1020.resourcekeeper.ui

import com.chilik1020.resourcekeeper.utils.JsonConfig
import org.jfree.chart.plot.XYPlot
import org.jfree.data.time.TimeSeries
import org.jfree.ui.tabbedui.VerticalLayout
import javax.swing.*

class JPanelOwenTemperatureChartsPanel : JPanelBaseTemperatureChartsPanel() {

    private var numberOfCharts = 0

    init {
        initComponents()
        if (!JsonConfig.owenEnable)
            disableTab()
    }

    override fun initFields() {
        layout = VerticalLayout()

        for (i in 0 until JsonConfig.tempChanelsOwen.size) {
            tempChCurrentsOnChart.add(JLabel())
            tempChMins.add(JFormattedTextField())
            tempChMaxs.add(JFormattedTextField())
            tempChTrackEns.add(JCheckBox())
            tempChanelNames.add(JsonConfig.tempChanelsOwen[i].name)
            val s = TimeSeries("${i+1}")
            listSeries.add(s)
            listPlots.add(XYPlot())
            if (JsonConfig.tempChanelsOwen[i].isEnable)
                numberOfCharts++
        }
        chartHeight = 509/ numberOfCharts
    }

    override fun cycleCreateAndInitializeChartItems() {
        for (i in 0 until JsonConfig.tempChanelsOwen.size) {
            if (JsonConfig.tempChanelsOwen[i].isEnable) {
                createChartItemPanel(i)
                tempChMins[i].value = JsonConfig.tempChanelsOwen[i].limitMin.toString()
                tempChMaxs[i].value = JsonConfig.tempChanelsOwen[i].limitMax.toString()
            }
            tempChMins[i].isEnabled = false
            tempChMaxs[i].isEnabled = false
            tempChTrackEns[i].isEnabled = false
        }
    }

    override fun changeStateElements(state: Boolean) {
        for (i in 0 until JsonConfig.tempChanelsOwen.size) {
            if(JsonConfig.tempChanelsOwen[i].isEnable) {
                tempChMins[i].isEnabled = state
                tempChMaxs[i].isEnabled = state
                tempChTrackEns[i].isEnabled = state
            }
        }
        rangeTempChoose.isEnabled = state
    }

}