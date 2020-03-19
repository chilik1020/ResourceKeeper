package di

import com.chilik1020.resourcekeeper.controller.energy.EnergyCommonLogController
import com.chilik1020.resourcekeeper.controller.energy.EnergyLabMaxController
import com.chilik1020.resourcekeeper.controller.general.GeneralController
import com.chilik1020.resourcekeeper.controller.telegram.TelegramController
import com.chilik1020.resourcekeeper.controller.temperature.BaseTemperatureController
import com.chilik1020.resourcekeeper.controller.temperature.LtuInnerTemperatureController
import com.chilik1020.resourcekeeper.controller.temperature.OwenTemperatureController
import com.chilik1020.resourcekeeper.di.DeviceModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [BufferModule::class, DeviceModule::class, ViewModule::class])
interface AppComponent{

    fun inject(controller: TelegramController)
    fun inject(controller: OwenTemperatureController)
    fun inject(controller: LtuInnerTemperatureController)
    fun inject(controller: EnergyCommonLogController)
    fun inject(controller: EnergyLabMaxController)
    fun inject(controller: GeneralController)
    fun inject(controller: BaseTemperatureController)

    @Component.Builder
    interface Builder {
        fun build(): AppComponent
        fun bufferModule(bufferModule: BufferModule):Builder
        fun deviceModule(deviceModule: DeviceModule):Builder
        fun viewModule(viewModule: ViewModule): Builder
    }

}