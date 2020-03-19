package com.chilik1020.resourcekeeper.controller.energy.listener

import com.chilik1020.resourcekeeper.controller.energy.EnergyCommonLogController
import com.chilik1020.resourcekeeper.ui.JPanelEnergy
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

class RangeCommonChooseListener(private val view: JPanelEnergy, private val controller: EnergyCommonLogController): ActionListener {

    override fun actionPerformed(e: ActionEvent?) {
        controller.setDomainRange(view.rangeXCommonChoose.selectedItem as String)
    }
}