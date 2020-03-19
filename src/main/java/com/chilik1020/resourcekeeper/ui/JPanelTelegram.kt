package com.chilik1020.resourcekeeper.ui


import com.chilik1020.resourcekeeper.controller.telegram.listener.BotChooseListener
import com.chilik1020.resourcekeeper.controller.telegram.listener.BotStartListener
import com.chilik1020.resourcekeeper.controller.telegram.TelegramController
import com.chilik1020.resourcekeeper.utils.ColorUtil
import java.awt.*
import javax.swing.JButton
import javax.swing.JComboBox
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.border.MatteBorder

class JPanelTelegram : JPanel() {

    private val fontSize16 = Font("Serif", Font.BOLD, 16)
    private val fontSize14 = Font("Serif", Font.BOLD, 14)
    val labelName = JLabel("Телеграм бот")
    internal val labelOnline = JLabel("Выберите бота")

    internal var botChoose: JComboBox<String> = JComboBox()
    internal var btnBotStart: JButton = JButton("Старт")

    init {
        isVisible = true

        initComponents()
    }

    private fun initComponents() {
        layout = GridBagLayout()
        border = MatteBorder(3, 1, 1, 1, Color.GRAY)

        (layout as GridBagLayout).columnWidths = intArrayOf(5, 110, 100, 100, 100, 180,5)

        labelName.font = fontSize16
        labelName.preferredSize = Dimension(110,20)
        add(labelName, GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                Insets(5, 5, 5, 5), 0, 0))

        labelOnline.font = fontSize14
        labelOnline.preferredSize = Dimension(100,20)
        add(labelOnline, GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                Insets(5, 5, 5, 5), 0, 0))

        botChoose.preferredSize = Dimension(150,20)
        add(botChoose, GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                Insets(5, 5, 5, 5), 0, 0))

        btnBotStart.background = ColorUtil.btnRedColor
        btnBotStart.preferredSize = Dimension(100,20)
        add(btnBotStart, GridBagConstraints(4, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                Insets(5, 5, 5, 5), 0, 0))

        btnBotStart.isEnabled = false
    }

    fun addListeners(controller: TelegramController) {
        botChoose.addActionListener(BotChooseListener(this, controller))
        btnBotStart.addActionListener(BotStartListener(this, controller))
    }
}