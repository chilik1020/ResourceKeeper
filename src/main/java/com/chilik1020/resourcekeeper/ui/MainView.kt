package com.chilik1020.resourcekeeper.ui

import com.chilik1020.resourcekeeper.controller.energy.EnergyCommonLogController
import com.chilik1020.resourcekeeper.controller.energy.EnergyLabMaxController
import com.chilik1020.resourcekeeper.controller.general.GeneralController
import com.chilik1020.resourcekeeper.controller.telegram.TelegramController
import com.chilik1020.resourcekeeper.controller.temperature.LtuInnerTemperatureController
import com.chilik1020.resourcekeeper.controller.temperature.OwenTemperatureController
import com.chilik1020.resourcekeeper.utils.chartPanelToPng
import java.awt.BorderLayout
import java.awt.Color
import javax.swing.BorderFactory
import javax.swing.JFrame
import javax.swing.JPanel

class MainView : JFrame() {

    private val owenTemperatureController = OwenTemperatureController()
    private val ltuTemperatureController = LtuInnerTemperatureController()
    private val energyCommonController = EnergyCommonLogController()
    private val energyLabMaxController = EnergyLabMaxController()
    private val telegramController = TelegramController()
    private val generalController = GeneralController()

    private lateinit var mainPanel: JPanel

    private val jPanelTelegram: JPanelTelegram
    val jPanelTemperature: JPanelTemperature
    val jPanelEnergy: JPanelEnergy
    val jPanelGeneral: JPanelGeneral


    init {
        title = "ResourceKeeper"
        defaultCloseOperation = EXIT_ON_CLOSE
        isVisible = true

        jPanelTelegram = telegramController.view
        jPanelTemperature = owenTemperatureController.viewTemperatureMain
        jPanelEnergy = energyCommonController.view
        jPanelGeneral = generalController.view
        initComponents()
    }

    private fun initComponents() {
        val container = contentPane

//        container.layout = BorderLayout()

        mainPanel = JPanel()
        mainPanel.apply {
            border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
        //    background = Color.DARK_GRAY
            layout = BorderLayout(5, 0)
        }

        val leftPanel = JPanel()
        leftPanel.apply {
       //     background = Color.DARK_GRAY
            layout = BorderLayout(0, 0)
        }

        leftPanel.add(jPanelTelegram, BorderLayout.NORTH)
        leftPanel.add(jPanelTemperature, BorderLayout.SOUTH)

        val rightPanel = JPanel()
        rightPanel.apply {
         //   background = Color.DARK_GRAY
            layout = BorderLayout(0, 5)
        }
        rightPanel.add(jPanelGeneral, BorderLayout.NORTH)
        rightPanel.add(jPanelEnergy, BorderLayout.SOUTH)

        mainPanel.add(leftPanel, BorderLayout.WEST)
        mainPanel.add(rightPanel, BorderLayout.EAST)

        container.add(mainPanel)

        pack()
    }

    fun saveScreenPng() {
        chartPanelToPng("screenwindow.png", mainPanel)
    }
}