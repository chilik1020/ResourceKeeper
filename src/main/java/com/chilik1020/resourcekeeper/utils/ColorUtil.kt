package com.chilik1020.resourcekeeper.utils

import java.awt.Color

class ColorUtil {
    companion object {
        val btnGreenColor: Color = Color(95,167,127)
        val btnRedColor: Color = Color(216,88,124)

        val panelBorderColor: Color = Color.gray

        val chartsPlotBackgroundColor: Color = Color(60,63,65)
        val chartsBackgroundColor: Color = Color(60,63,65)
//        val chartsBackgroundColor: Color = Color(243,243,243)
        val labMaxChartColor: Color = Color(86,180,99)
        val labMaxDetailsEnergyColor = Color(0,255,255)
        val labMaxDetailsEnergyColor2 = Color(180,255,255)
        val labMaxDetailsShotColor = Color(0,255,1)
        val commonDetailsShotColor = Color(255,242,64)

        val commonChartMeanColor = Color(93,86,180)
        val commonChartStartColor = Color(86,180,99)
        val commonChartStopColor = Color(195,51,36)

        val temperatureChartsColor = mutableListOf<Color>()

        init {
            temperatureChartsColor.add(Color(93,86,180))
            temperatureChartsColor.add(Color(86,180,99))
            temperatureChartsColor.add(Color(86,163,180))
            temperatureChartsColor.add(Color(195,51,36))

            temperatureChartsColor.add(Color(195,126,36))
            temperatureChartsColor.add(Color(180,86,177))
            temperatureChartsColor.add(Color(51,55,14))
            temperatureChartsColor.add(Color(5,115,119))
        }
    }
}