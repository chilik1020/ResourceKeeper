package com.chilik1020.resourcekeeper.controller.temperature.listener

import com.chilik1020.resourcekeeper.controller.temperature.BaseTemperatureController
import com.chilik1020.resourcekeeper.model.data.AlertLevel
import com.chilik1020.resourcekeeper.model.data.AlertMessage
import com.chilik1020.resourcekeeper.ui.JPanelBaseTemperatureChartsPanel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.chilik1020.resourcekeeper.utils.JsonConfig
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.lang.Exception

class BaseTemperatureLimitTracker(private val view: JPanelBaseTemperatureChartsPanel,
                                  private val controller: BaseTemperatureController) : ActionListener {

    private var minLimits = mutableListOf<Double>()
    private var maxLimits = mutableListOf<Double>()

    private var isTrackingEn = false
    private var isLimitExceeded = false

    init {
        for (i in 0..7) {
            minLimits.add(0.0)
            maxLimits.add(0.0)
        }
    }

    override fun actionPerformed(e: ActionEvent?) {
        var isTrackNeeded = false

        for (i in 0 until view.tempChTrackEns.size) {
                if (view.tempChTrackEns[i].isSelected) {
                    try {
                        minLimits[i] = view.tempChMins[i].value.toString().toDouble()
                        maxLimits[i] = view.tempChMaxs[i].value.toString().toDouble()
                        isTrackNeeded = true
                        changeFieldState(i, false)
                    }catch (ex: Exception) {
                        view.tempChTrackEns[i].isSelected = false
                        view.createAlert("Ошибка", "Некорректное значение")
                    }
                }
                else
                    changeFieldState(i, true)
        }

        if (isTrackNeeded && !isTrackingEn) {
            isTrackingEn = true
            startTracking()
        }
    }

    open fun startTracking() {
        GlobalScope.launch {
            while (isTrackingEn) {
                for (i in 0 until view.tempChTrackEns.size) {
                    if (view.tempChTrackEns[i].isSelected) {
                        try {
                            val current = controller.currentTemp[i]
                            val minLimit = minLimits[i]
                            val maxLimit = maxLimits[i]

                            if (current < minLimit || current > maxLimit){
                                isLimitExceeded = true
                                controller.alertSystem.add(AlertMessage(AlertLevel.ERROR, "Temperature chanel ${i+1} ($current °C) isn't in valid range [$minLimit ... $maxLimit]", view))
                                view.tempChTrackEns[i].isSelected = false
                                changeFieldState(i, true)
                            }

                        } catch (ex: Exception) {
                            isTrackingEn = false
                        }

                    }
                }
                delay(1000)
            }
        }
    }

    open fun changeFieldState(i: Int, state: Boolean) {
        view.tempChMins[i].isEnabled = state
        view.tempChMaxs[i].isEnabled = state
    }
}