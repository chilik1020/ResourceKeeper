package di

import dagger.Module
import dagger.Provides
import com.chilik1020.resourcekeeper.model.DataBuffer
import com.chilik1020.resourcekeeper.model.data.*
import javax.inject.Named
import javax.inject.Singleton

@Module
object BufferModule {

    @Singleton
    @Provides
    @Named("ltuInner")
    fun provideBufferLtuTemperature(): DataBuffer<TemperaturePoint> {
        return DataBuffer()
    }

    @Singleton
    @Provides
    @Named("owen")
    fun provideBufferTemperature(): DataBuffer<TemperaturePoint> {
        return DataBuffer()
    }

    @Singleton
    @Provides
    fun provideBufferEnergyShot(): DataBuffer<EnergyShot> {
        return DataBuffer()
    }

    @Singleton
    @Provides
    fun provideBufferEnergyCycle(): DataBuffer<EnergyCycle> {
        return DataBuffer()
    }

    @Singleton
    @Provides
    fun provideBufferBotEventMessage(): DataBuffer<BotEventMessage> {
        return DataBuffer()
    }
}