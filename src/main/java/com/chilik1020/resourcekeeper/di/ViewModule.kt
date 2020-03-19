package di

import dagger.Module
import dagger.Provides
import com.chilik1020.resourcekeeper.ui.JPanelEnergy
import com.chilik1020.resourcekeeper.ui.JPanelGeneral
import com.chilik1020.resourcekeeper.ui.JPanelTelegram
import com.chilik1020.resourcekeeper.ui.JPanelTemperature
import javax.inject.Singleton

@Singleton
@Module
object ViewModule {

    @Singleton
    @Provides
    fun jPanelTemperature(): JPanelTemperature {
        return JPanelTemperature()
    }

    @Singleton
    @Provides
    fun jPanelEnergy(): JPanelEnergy {
        return JPanelEnergy()
    }

    @Singleton
    @Provides
    fun jPanelTelegram(): JPanelTelegram {
        return JPanelTelegram()
    }

    @Singleton
    @Provides
    fun jPanelGeneral(): JPanelGeneral {
        return JPanelGeneral()
    }

}