package com.chilik1020.resourcekeeper.controller.energy.listener

import com.chilik1020.resourcekeeper.controller.energy.EnergyCommonLogController
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import com.chilik1020.resourcekeeper.ui.JPanelEnergy
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.io.File
import javax.swing.JFileChooser

class EnergyCommonLogFileChooseListener(private val view: JPanelEnergy, private val controller: EnergyCommonLogController): ActionListener {

    private lateinit var file: File
    private var readEn = false
    private var logReaderEn = false

    private val delay = 1000L

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
                view.commonFilePath.text = path

                file = File(path)
                if (!file.exists())
                    file.createNewFile()
                controller.setLogFile(path)
                start()
            }

        } else if (e.actionCommand.startsWith("Сбросить")) {
            stop()
        }
    }

    private fun start() {
        controller.consumersBufferResource.add(controller)
        if (file.length() != 0L) {
            controller.start()
            logReaderEn = true
        }

        readEn = true
        readBuffer()

        controller.resourceLogWriter.file = file
        view.commonFileChooseButton.text = "Сбросить"
        view.energyFileChooseButton.isEnabled = true

        view.commonElementsState(true)
    }

    private fun readBuffer(){
        GlobalScope.launch {
            delay(1000L)
            while (readEn) {
                if (controller.buffer.getSize() != 0) {
                    val data = controller.buffer.poll()
                    for (c in controller.consumersBufferResource)
                        c.consume(data)

                    if (logReaderEn) {
                        controller.resourceLogWriter.currentResource = data.fullResource
                    }
                } else {
                    logReaderEn = false
                    delay(delay)
                }
            }
        }
    }

    private fun stop() {
        readEn = false

        controller.stop()
        controller.consumersBufferResource.clear()
        view.seriesCommonStartEnergy.clear()
        view.seriesCommonStopEnergy.clear()
        view.seriesCommonMeanEnergy.clear()

        view.commonElementsState(false)

        view.energyFileChooseButton.isEnabled = false
        view.commonFilePath.isEnabled = true
        view.commonFileChooseButton.text = "Выбрать"
        view.commonFilePath.text = ""
        view.resourceValue.text = "0"

    }

}