package com.chilik1020.resourcekeeper.controller.telegram

import com.chilik1020.resourcekeeper.base.BaseController
import com.chilik1020.resourcekeeper.base.Consumer
import com.chilik1020.resourcekeeper.model.AlertSystem
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.chilik1020.resourcekeeper.model.DataBuffer
import com.chilik1020.resourcekeeper.model.data.AlertLevel
import com.chilik1020.resourcekeeper.model.data.AlertMessage
import com.chilik1020.resourcekeeper.model.data.BotEventMessage
import com.chilik1020.resourcekeeper.model.telegrambot.BotReceiver
import com.chilik1020.resourcekeeper.model.telegrambot.BotTransmitter
import com.chilik1020.resourcekeeper.ui.JPanelTelegram
import com.chilik1020.resourcekeeper.utils.ColorUtil
import com.chilik1020.resourcekeeper.utils.JsonConfig
import com.chilik1020.resourcekeeper.utils.data.BotSettings
import com.chilik1020.resourcekeeper.utils.isConnectionGood
import javax.inject.Inject

class TelegramController : BaseController(){

    @Inject lateinit var view: JPanelTelegram
    @Inject lateinit var botReceiver: BotReceiver
    @Inject lateinit var botTransmitter: BotTransmitter

    @Inject lateinit var buffer: DataBuffer<BotEventMessage>
    @Inject lateinit var alertSystem: AlertSystem

    val consumers = mutableListOf<Consumer<BotEventMessage>>()
    lateinit var botSettings: BotSettings
    private var isBotRunning = false

    private var isUserNotified = false


    init {
        view.addListeners(this)
        val bots = JsonConfig.bots
        bots.forEach {
            view.botChoose.addItem(it.name)
        }

        checkConnection()
    }

    fun start() {
        botTransmitter.setSource(botSettings.token)
        botReceiver.setSource(botSettings.token)
        botReceiver.setReadEn(true)
        isBotRunning = true

        GlobalScope.launch {
            botReceiver.startReading()
        }

        GlobalScope.launch {
            while (isBotRunning) {
                if(alertSystem.getSize() != 0) {
                    val data = alertSystem.poll()
                    alertSystem.createAlert(data)
                } else
                    delay(1000)
            }
        }
    }

    fun stop(){
        botReceiver.setReadEn(false)
        isBotRunning = false
    }

    private fun checkConnection() {
        GlobalScope.launch {
            while (true) {
                val isConnectionGood = isConnectionGood()

                if (!isConnectionGood && !isUserNotified) {
                    if(!isUserNotified) {
                        alertSystem.add(AlertMessage(AlertLevel.WARNING, "No Internet connection", view))
                        isUserNotified = true
                    }

                    view.labelOnline.text = "Оффлайн"
                    view.labelOnline.foreground = ColorUtil.btnRedColor

                } else if (isConnectionGood) {
                    if (isUserNotified) {
                        alertSystem.add(AlertMessage(AlertLevel.INFO, "Connection restored", view))
                        isUserNotified = false
                    }

                    view.labelOnline.text = "Онлайн"
                    view.labelOnline.foreground = ColorUtil.btnGreenColor
                }

                delay(2000)
            }
        }
    }
}