package com.chilik1020.resourcekeeper.ui

import com.chilik1020.resourcekeeper.utils.JsonConfig
import org.jfree.chart.plot.XYPlot
import org.jfree.data.time.TimeSeries
import org.jfree.ui.tabbedui.VerticalLayout
import javax.swing.JCheckBox
import javax.swing.JFormattedTextField
import javax.swing.JLabel

class JPanelLtuInnerTemperatureChartsPanel : JPanelBaseTemperatureChartsPanel() {

    init {
        initComponents()
        if (!JsonConfig.innerSensorsEn)
            disableTab()
    }

    override fun initFields() {
        layout = VerticalLayout()
        for (i in 0 until 3) {
            tempChCurrentsOnChart.add(JLabel())
            tempChMins.add(JFormattedTextField())
            tempChMaxs.add(JFormattedTextField())
            tempChTrackEns.add(JCheckBox())
            tempChanelNames.add(JsonConfig.tempChanelsLtu[i].name)
            val s = TimeSeries("${i+1}")
            listSeries.add(s)
            listPlots.add(XYPlot())
        }
        chartHeight = 180
    }

    override fun cycleCreateAndInitializeChartItems() {
        for (i in 0 until JsonConfig.tempChanelsLtu.size) {
            createChartItemPanel(i)
            tempChMins[i].value = JsonConfig.tempChanelsLtu[i].limitMin.toString()
            tempChMaxs[i].value = JsonConfig.tempChanelsLtu[i].limitMax.toString()
            tempChMins[i].isEnabled = false
            tempChMaxs[i].isEnabled = false
            tempChTrackEns[i].isEnabled = false
        }
    }

    override fun changeStateElements(state: Boolean) {
        for (i in 0 until JsonConfig.tempChanelsLtu.size) {
            tempChMins[i].isEnabled = state
            tempChMaxs[i].isEnabled = state
            tempChTrackEns[i].isEnabled = state
        }

        rangeTempChoose.isEnabled = state
    }

}