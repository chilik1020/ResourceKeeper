package com.chilik1020.resourcekeeper.controller.energy.listener

import com.chilik1020.resourcekeeper.controller.energy.EnergyLabMaxController
import com.chilik1020.resourcekeeper.ui.JPanelEnergy
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

class RangeLabMaxChooseListener(private val view: JPanelEnergy, private val controller: EnergyLabMaxController): ActionListener {

    override fun actionPerformed(e: ActionEvent?) {
        controller.setDomainRange(view.rangeXLabMaxChoose.selectedItem as String)
    }
}