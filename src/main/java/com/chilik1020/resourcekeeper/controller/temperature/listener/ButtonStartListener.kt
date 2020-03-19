package com.chilik1020.resourcekeeper.controller.temperature.listener

import com.chilik1020.resourcekeeper.base.Consumer
import com.chilik1020.resourcekeeper.controller.temperature.BaseTemperatureController
import com.chilik1020.resourcekeeper.model.data.TemperaturePoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import com.chilik1020.resourcekeeper.ui.JPanelBaseTemperatureChartsPanel
import com.chilik1020.resourcekeeper.utils.ColorUtil
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

class ButtonStartListener(private val view: JPanelBaseTemperatureChartsPanel,
                          private val controller: BaseTemperatureController): ActionListener {

    private val consumers = mutableListOf<Consumer<TemperaturePoint>>()

    private var readEn = false

    override fun actionPerformed(e: ActionEvent?) {
        if (e == null)
            return
        if (e.actionCommand == null)
            return

        if (e.actionCommand.startsWith("Старт")) {
            start()
        } else if (e.actionCommand.startsWith("Стоп")) {
            stop()
        }
    }


    private fun start() {
//        controller.temperatureLogWriter.setSource()

        consumers.add(controller)
//        consumers.add(controller.commonLogWriter)
//
//        if(view.logOn.isSelected)
//            consumers.add(controller.temperatureLogWriter)

        controller.startReading()
        view.btnStart.text = "Стоп"
        readEn = true

        readBuffer()

        view.btnStart.background = ColorUtil.btnGreenColor
        view.changeStateElements(true)
        view.sourceChoose.isEnabled = false
        view.btnResetChart.isEnabled = false
    }

    private fun readBuffer() {
        GlobalScope.launch {
            while (readEn) {
                if(controller.getDataBuffer().getSize() != 0) {
                    val data = controller.getDataBuffer().poll()
                    for (c in consumers)
                        c.consume(data)
                } else
                    delay(900)
            }
        }
    }

    private fun stop() {
        controller.stopReading()
        consumers.clear()
        readEn = false
        view.btnStart.text = "Завершение..."

        GlobalScope.launch {
            delay(2000)
            view.btnStart.text = "Старт"
            view.btnStart.background = ColorUtil.btnRedColor
            view.changeStateElements(false)
            view.sourceChoose.isEnabled = true
            view.btnResetChart.isEnabled = true
//            view.logOn.isEnabled = true
        }


    }
}