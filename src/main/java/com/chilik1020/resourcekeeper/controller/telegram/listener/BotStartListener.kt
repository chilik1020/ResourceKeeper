package com.chilik1020.resourcekeeper.controller.telegram.listener

import com.chilik1020.resourcekeeper.controller.telegram.TelegramController
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.chilik1020.resourcekeeper.ui.JPanelTelegram
import com.chilik1020.resourcekeeper.utils.ColorUtil
import com.chilik1020.resourcekeeper.utils.JsonConfig
import com.chilik1020.resourcekeeper.utils.isConnectionGood
import java.awt.Color
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

class BotStartListener(private val view: JPanelTelegram, private val controller: TelegramController) : ActionListener {

    private var readEn = false
    private val delay = 500L

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

    private fun start(){
        controller.consumers.add(controller.botTransmitter)
        val selectedBot = view.botChoose.selectedIndex
        controller.botSettings = JsonConfig.bots[selectedBot]
        readEn = true
        controller.start()

        readBuffer()
        view.btnBotStart.text = "Стоп"
        view.btnBotStart.background = ColorUtil.btnGreenColor
        view.botChoose.isEnabled = false
    }

    private fun readBuffer() {
        GlobalScope.launch {
            while (readEn) {

                if (controller.buffer.getSize()!= 0) {
                    val ep = controller.buffer.poll()
                    for (c in controller.consumers)
                        c.consume(ep)
                } else
                    delay(delay)
            }
        }
    }

    private fun stop() {
        readEn = false
        controller.stop()
        controller.consumers.clear()

        view.btnBotStart.text = "Завершение..."

        GlobalScope.launch {
            delay(2000)
            view.btnBotStart.background = ColorUtil.btnRedColor
            view.btnBotStart.text = "Старт"
            view.botChoose.isEnabled = true
        }
    }

}