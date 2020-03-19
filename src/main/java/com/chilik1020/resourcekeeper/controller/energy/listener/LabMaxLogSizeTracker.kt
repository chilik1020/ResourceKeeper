package com.chilik1020.resourcekeeper.controller.energy.listener

import com.chilik1020.resourcekeeper.controller.energy.EnergyLabMaxController
import com.chilik1020.resourcekeeper.model.data.AlertLevel
import com.chilik1020.resourcekeeper.model.data.AlertMessage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.chilik1020.resourcekeeper.ui.JPanelEnergy
import com.chilik1020.resourcekeeper.utils.ColorUtil
import com.chilik1020.resourcekeeper.utils.JsonConfig
import java.awt.Color
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.io.File
import javax.swing.JOptionPane

class LabMaxLogSizeTracker(private val view: JPanelEnergy, private val controller: EnergyLabMaxController):ActionListener {

    private var trackEn = false
    private var isLogCrashed = false
    private var fileLength = 0L

    override fun actionPerformed(e: ActionEvent?) {
        trackEn = view.logSizeTrackEn.isSelected
        if (trackEn) {
            isLogCrashed = false
            startTracking()
        }
    }

    private fun startTracking() {
        GlobalScope.launch {
            var count = 0L
            while (trackEn) {
                val currentLength = File(controller.filePath).length()
                view.logSize.text = currentLength.toString()

                count = if (currentLength == fileLength){ count + 1} else 0
                if (count == JsonConfig.sleepCycle+20) {
                    if (!isLogCrashed) {
                        controller.alertSystem.add(AlertMessage(AlertLevel.ERROR, "LabMax's log is not updated", view))
                        view.logSizeTrackEn.isSelected = false
                    }

                    isLogCrashed = true
                }

                fileLength = currentLength


                delay(1000)
                if (!view.logSizeTrackEn.isSelected)
                    trackEn = false
            }
        }
    }
}