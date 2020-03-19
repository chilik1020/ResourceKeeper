package com.chilik1020.resourcekeeper.controller.telegram.listener

import com.chilik1020.resourcekeeper.controller.telegram.TelegramController
import com.chilik1020.resourcekeeper.ui.JPanelTelegram
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

class BotChooseListener(private val view: JPanelTelegram, private val controller: TelegramController) : ActionListener {

    override fun actionPerformed(e: ActionEvent?) {
        view.btnBotStart.isEnabled = view.botChoose.getItemAt(0) != null
    }

}