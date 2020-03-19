package com.chilik1020.resourcekeeper.controller.energy.listener

import com.chilik1020.resourcekeeper.controller.energy.EnergyLabMaxController
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.chilik1020.resourcekeeper.ui.JPanelEnergy
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.io.File
import javax.swing.JFileChooser

class EnergyLabMaxFileChooseListener(private val view: JPanelEnergy, private val controller: EnergyLabMaxController): ActionListener {

    private var readEn = false
    private val delay = 500L

    override fun actionPerformed(e: ActionEvent?) {
        if (e == null)
            return
        if (e.actionCommand == null)
            return

        if (e.actionCommand.startsWith("Выбрать")) {
            val chooseFile = JFileChooser()
            chooseFile.currentDirectory = File("")
            val ret = chooseFile.showDialog(null, "Выбрать")

            if (ret == JFileChooser.APPROVE_OPTION) {
                val path = chooseFile.selectedFile.absolutePath
                view.energyFilePath.text = path
                controller.setLogFile(path)
                start()
            }

        } else if (e.actionCommand.startsWith("Сбросить")) {
            stop()
        }

    }

    private fun start() {
        controller.consumersBufferLabMax.add(controller)
        controller.consumersBufferLabMax.add(controller.energyProcess)

        controller.startReading()

        view.commonFileChooseButton.isEnabled = false
        view.labMaxElementsState(true)
        view.energyFileChooseButton.text = "Сбросить"
        readEn = true

        readBuffer()
    }

    private fun readBuffer() {
        GlobalScope.launch {
            while (readEn) {
                if (controller.buffer.getSize()!= 0) {
                    val ep = controller.buffer.poll()
                    for (c in controller.consumersBufferLabMax)
                        c.consume(ep)
                } else
                    delay(delay)
            }
        }
    }

    private fun stop() {
        controller.stopReading()
        readEn = false
        controller.consumersBufferLabMax.clear()
        view.energyFileChooseButton.text = "Выбрать"
        view.energyFilePath.text = ""
        view.energyCurrent.text = "0"
        view.shotCurrent.text = "0"
        view.energyMin.text = "0"
        view.energyMax.text = "0"
        view.energyMean.text = "0"

        view.labMaxElementsState(false)
//        view.energyTrackEn.isSelected = false
        view.energyFileChooseButton.isEnabled = true
        view.commonFileChooseButton.isEnabled = true
        view.logSizeTrackEn.isSelected = false
        view.logSize.text = ""
    }

}