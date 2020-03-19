package com.chilik1020.resourcekeeper.model.telegrambot

import com.chilik1020.resourcekeeper.base.Producer
import com.chilik1020.resourcekeeper.model.DataBuffer
import com.chilik1020.resourcekeeper.model.data.BotEventMessage
import com.chilik1020.resourcekeeper.model.data.BotEventMessageType
import com.chilik1020.resourcekeeper.model.telegrambot.exceptions.TgApiException
import com.chilik1020.resourcekeeper.model.telegrambot.types.Message
import com.chilik1020.resourcekeeper.utils.JsonConfig
import com.chilik1020.resourcekeeper.utils.isConnectionGood

class BotReceiver(val buffer: DataBuffer<BotEventMessage>): Producer<BotEventMessage> {

    private val bot: Bot = Bot("")
    private lateinit var token: String
    private lateinit var name: String
    private var readEn = false
    private val botCommands = mutableMapOf<String, String>()

    private var offset = 0

    override fun startReading() {
        println("INFO: Bot (${name}) started")
        while (readEn) {

            try {
                if (!isConnectionGood()) {
                    Thread.sleep(500)
                    continue
                }


                val updates = bot.getUpdates(offset, 10, 1) ?: continue

                updates.forEach {
                    if (it != null) {
                        offset = it.update_id + 1

                        val msg = it.message
                        val text = msg.text ?: "invalid command"

                        var isValidCommand = false
                        var command = ""
                        for(c in botCommands.keys) {
                            if (text.startsWith(c)) {
                                isValidCommand = true
                                command = c
                            }
                        }

                        if (isValidCommand){
                            val event = parseMessage(command, msg)
                            produce(event)
                        }
                    }
                }
            } catch (ex: TgApiException) {
                println("WARNING: ${ex.message}")
            }

        }
        println("INFO: Bot (${name}) stopped")
    }

    override fun produce(t: BotEventMessage) {
        buffer.add(t)
    }

    override fun setSource(source: String) {
        this.token = source
        this.bot.setToken(source)
        for (b in JsonConfig.bots) {
            if (b.token == this.token) {
                this.name = b.name
                for (c in b.commands){
                    botCommands[c.name] = c.path
                }
            }
        }
    }

    override fun getAvailable(): Array<String> {
        return arrayOf()
    }

    override fun setReadEn(state: Boolean) {
        this.readEn = state
        if (!state)
            botCommands.clear()
    }

    private fun parseMessage(command: String, msg: Message): BotEventMessage {
        val chatId = msg.chat.id
        val value = botCommands[command]
        val type = BotEventMessageType.getTypeByString(command)
        var event: BotEventMessage

        var isAllowedChatId = false
        for(u in JsonConfig.allowedUsers)
            if(u.chatId.toInt() == chatId){
                isAllowedChatId = true
                break
            }

        if (isAllowedChatId)
            event = BotEventMessage(type, command, value!!, chatId)
        else {
            event = BotEventMessage(BotEventMessageType.MESSAGE, command, "Неразрешенный запрос.", chatId)
            println("WARNING: Unresolved request [$event]")
        }
        return event
    }
}