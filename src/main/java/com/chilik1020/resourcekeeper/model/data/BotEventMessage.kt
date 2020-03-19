package com.chilik1020.resourcekeeper.model.data

data class BotEventMessage(
        val type: BotEventMessageType,
        val command: String,
        val message: String,
        val chatId: Int

)

enum class BotEventMessageType {
    MESSAGE,
    PHOTO,
    DOC;

    companion object {
        fun getTypeByString(str: String): BotEventMessageType {
            var et = MESSAGE
            when (str) {
                "/test" -> et = MESSAGE

                "/labmax" -> et = PHOTO

                "/picall" -> et = PHOTO

                "/realpicall" -> et = PHOTO

                "/screen" -> et = PHOTO

                "/temperature" -> et = PHOTO

                "Screen" -> et = DOC

                "Reset shift" -> et = MESSAGE

                "/underground" -> et = DOC

                "Get mem" -> et = PHOTO
            }
            return et
        }
    }
}