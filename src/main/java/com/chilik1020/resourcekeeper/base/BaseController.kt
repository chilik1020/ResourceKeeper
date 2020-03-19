package com.chilik1020.resourcekeeper.base

import com.chilik1020.resourcekeeper.controller.energy.EnergyCommonLogController
import com.chilik1020.resourcekeeper.controller.energy.EnergyLabMaxController
import com.chilik1020.resourcekeeper.controller.general.GeneralController
import com.chilik1020.resourcekeeper.controller.telegram.TelegramController
import com.chilik1020.resourcekeeper.controller.temperature.LtuInnerTemperatureController
import com.chilik1020.resourcekeeper.controller.temperature.OwenTemperatureController

abstract class BaseController {
    private val injectorComponent = MainApp.injectorComponent

    init {
        inject()
    }

    private fun inject() {
        when(this) {
            is TelegramController -> injectorComponent.inject(this)
            is OwenTemperatureController -> injectorComponent.inject(this)
            is LtuInnerTemperatureController -> injectorComponent.inject(this)
            is EnergyCommonLogController -> injectorComponent.inject(this)
            is EnergyLabMaxController -> injectorComponent.inject(this)
            is GeneralController -> injectorComponent.inject(this)
        }
    }
}