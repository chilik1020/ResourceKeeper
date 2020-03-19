package com.chilik1020.resourcekeeper.controller.temperature.listener

import com.chilik1020.resourcekeeper.controller.temperature.BaseTemperatureController
import com.chilik1020.resourcekeeper.ui.JPanelBaseTemperatureChartsPanel
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

class RangeTempChooseListener(private val view: JPanelBaseTemperatureChartsPanel, private val controller: BaseTemperatureController): ActionListener {

    override fun actionPerformed(e: ActionEvent?) {
        controller.setDomainRange(view.rangeTempChoose.selectedItem as String)
    }
}