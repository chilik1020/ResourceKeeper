package com.chilik1020.resourcekeeper.di

import com.chilik1020.resourcekeeper.model.AlertSystem
import com.chilik1020.resourcekeeper.model.DataBuffer
import com.chilik1020.resourcekeeper.model.EnergyProcess
import dagger.Module
import dagger.Provides
import com.chilik1020.resourcekeeper.model.data.BotEventMessage
import com.chilik1020.resourcekeeper.model.data.EnergyCycle
import com.chilik1020.resourcekeeper.model.data.EnergyShot
import com.chilik1020.resourcekeeper.model.data.TemperaturePoint
import com.chilik1020.resourcekeeper.model.log.CommonLogReader
import com.chilik1020.resourcekeeper.model.log.CommonLogWriter
import com.chilik1020.resourcekeeper.model.log.LabMaxLogReader
import com.chilik1020.resourcekeeper.model.log.TemperatureLogWriter
import com.chilik1020.resourcekeeper.model.telegrambot.BotReceiver
import com.chilik1020.resourcekeeper.model.telegrambot.BotTransmitter
import com.chilik1020.resourcekeeper.model.temperature.FakeTemperatureDevice
import com.chilik1020.resourcekeeper.model.temperature.LtuInnerTemperatureDevice
import com.chilik1020.resourcekeeper.model.temperature.RS232TemperatureDevice
import javax.inject.Named
import javax.inject.Singleton

@Module
object DeviceModule {

    @Provides
    @Singleton
    fun provideLtuInnerTemperatureDevice(@Named("ltuInner") buffer: DataBuffer<TemperaturePoint>): LtuInnerTemperatureDevice {
        return LtuInnerTemperatureDevice(buffer)
    }

    @Provides
    @Singleton
    fun provideTemperatureDevice(@Named("owen") buffer: DataBuffer<TemperaturePoint>): RS232TemperatureDevice {
        return RS232TemperatureDevice(buffer)
    }

    @Provides
    @Singleton
    fun provideFakeTemperatureDevice(@Named("owen") buffer: DataBuffer<TemperaturePoint>): FakeTemperatureDevice {
        return FakeTemperatureDevice(buffer)
    }

    @Provides
    @Singleton
    fun provideLabMaxReader(buffer: DataBuffer<EnergyShot>): LabMaxLogReader {
        return LabMaxLogReader(buffer)
    }

    @Provides
    @Singleton
    fun provideCommonReader(buffer: DataBuffer<EnergyCycle>): CommonLogReader {
        return CommonLogReader(buffer)
    }

    @Provides
    @Singleton
    fun provideCommonWriter(buffer: DataBuffer<EnergyCycle>): CommonLogWriter {
        return CommonLogWriter(buffer)
    }

    @Provides
    @Singleton
    fun provideTemperatureWriter(): TemperatureLogWriter {
        return TemperatureLogWriter()
    }

    @Provides
    @Singleton
    fun provideEnergyProcess(logWriter: CommonLogWriter): EnergyProcess {
        return EnergyProcess(logWriter)
    }

    @Provides
    @Singleton
    fun provideBotReceiver(buffer: DataBuffer<BotEventMessage>): BotReceiver {
        return BotReceiver(buffer)
    }

    @Provides
    @Singleton
    fun provideBotTransmitter(buffer: DataBuffer<BotEventMessage>): BotTransmitter {
        return BotTransmitter(buffer)
    }

    @Provides
    @Singleton
    fun provideAlertSystem(buffer: DataBuffer<BotEventMessage>): AlertSystem {
        return AlertSystem(buffer)
    }


}