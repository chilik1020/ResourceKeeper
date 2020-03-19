package com.chilik1020.resourcekeeper.model.telegrambot

import com.chilik1020.resourcekeeper.base.Consumer
import com.chilik1020.resourcekeeper.model.DataBuffer
import com.chilik1020.resourcekeeper.model.data.BotEventMessage
import com.chilik1020.resourcekeeper.model.data.BotEventMessageType
import com.chilik1020.resourcekeeper.utils.makeScreenShot
import java.io.File
import java.lang.Exception

class BotTransmitter(val buffer: DataBuffer<BotEventMessage>): Consumer<BotEventMessage> {

    private lateinit var bot: Bot
    private lateinit var token: String


    override fun consume(t: BotEventMessage) {
        try {
            when(t.type) {
                BotEventMessageType.MESSAGE -> {
                    if (t.command == "Reset shift") {
                        MainApp.mainView.jPanelGeneral.name1TextField.text = ""
                        MainApp.mainView.jPanelGeneral.name2TextField.text = ""
                    }

                    bot.sendMessage(t.chatId, t.message, null, null, null)
                }
                BotEventMessageType.PHOTO -> {
                    when(t.command){
                        "/labmax" -> MainApp.mainView.jPanelEnergy.saveLabMaxChartAsPng()
                        "/screen" -> MainApp.mainView.saveScreenPng()
                    }

                    val file = File(t.message)
                    if (file.exists())
                        bot.sendPhoto(t.chatId, file, t.message, null, null, null)
                    else {
                        bot.sendMessage(t.chatId, "Что-то пошло не так", null, null, null)
                        println("WARNING: $file doesn't exist.")
                    }
                }
                BotEventMessageType.DOC -> {
                    if (t.command == "Screen")
                        makeScreenShot(t.message)

                    val file = File(t.message)
                    if (file.exists())
                        bot.sendDocument(t.chatId, file, t.message, null, null)
                    else {
                        bot.sendMessage(t.chatId, "Что-то пошло не так", null, null, null)
                        println("WARNING: $file doesn't exist.")
                    }
                }
            }
        } catch (ex: Exception) {
            println("ERROR: ${ex.message}")
        }
    }

    override fun onDestroyConsumer() {
    }

    fun setSource(source: String) {
        this.token = source
        this.bot = Bot(source)
    }

}