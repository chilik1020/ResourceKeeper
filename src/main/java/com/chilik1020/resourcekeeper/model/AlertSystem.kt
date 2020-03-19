package com.chilik1020.resourcekeeper.model

import com.chilik1020.resourcekeeper.model.data.AlertLevel
import com.chilik1020.resourcekeeper.model.data.AlertMessage
import com.chilik1020.resourcekeeper.model.data.BotEventMessage
import com.chilik1020.resourcekeeper.model.data.BotEventMessageType
import com.chilik1020.resourcekeeper.utils.JsonConfig
import com.chilik1020.resourcekeeper.utils.getDateTime
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentLinkedQueue
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.sound.sampled.Clip
import javax.sound.sampled.AudioSystem
import java.io.File
import javax.sound.sampled.AudioInputStream



class AlertSystem(private val botMessageBuffer: DataBuffer<BotEventMessage>) {

    private val buffer = ConcurrentLinkedQueue<AlertMessage>()

    private var chatId = 0

    init {
        if (JsonConfig.allowedUsers.isNotEmpty())
            chatId = JsonConfig.allowedUsers[0].chatId.toInt()
    }

    fun add(data: AlertMessage) {
        buffer.add(data)
    }

    fun poll(): AlertMessage {
        return buffer.poll()
    }

    fun getSize(): Int {
        return buffer.size
    }

    fun clear() {
        buffer.clear()
    }

    fun createAlert(alert: AlertMessage) {
        val time = getDateTime()
        println("${alert.type}: $time,  ${alert.message}")

        if(alert.type == AlertLevel.ERROR) {
            botMessageBuffer.add(BotEventMessage(BotEventMessageType.MESSAGE,"", "${alert.type}: ${alert.message}", chatId))
            createAlert(alert.view, alert.type.toString(), alert.message)
        }
    }

    private fun createAlert(view: JPanel, title: String, message: String) {

        var n = -1

        GlobalScope.launch {
            val audioInputStream: AudioInputStream? = AudioSystem.getAudioInputStream(File(JsonConfig.alarmSound).absoluteFile)
            val clip: Clip? = AudioSystem.getClip()
            clip?.open(audioInputStream)
            clip?.loop(Clip.LOOP_CONTINUOUSLY)

            while (n == -1) {
                clip?.start()
                delay(1000)
            }
            clip?.stop()
        }

        n = JOptionPane.showConfirmDialog(
                view,
                message,
                title,
                JOptionPane.YES_NO_OPTION)
    }



}