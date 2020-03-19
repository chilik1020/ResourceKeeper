package com.chilik1020.resourcekeeper.controller.temperature

import com.chilik1020.resourcekeeper.controller.temperature.listener.BaseTemperatureLimitTracker
import com.chilik1020.resourcekeeper.model.DataBuffer
import com.chilik1020.resourcekeeper.model.data.TemperaturePoint
import com.chilik1020.resourcekeeper.model.log.CommonLogWriter
import com.chilik1020.resourcekeeper.model.log.TemperatureLogWriter
import com.chilik1020.resourcekeeper.model.temperature.LtuInnerTemperatureDevice
import com.chilik1020.resourcekeeper.ui.JPanelTemperature
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

class LtuInnerTemperatureController : BaseTemperatureController() {


    @Inject lateinit var viewTemperatureMain: JPanelTemperature
    @Inject lateinit var sourceDevice: LtuInnerTemperatureDevice
    @Inject lateinit var commonLogWriter: CommonLogWriter
    @Inject lateinit var temperatureLogWriter: TemperatureLogWriter
    @field:[Inject Named("ltuInner")]
    lateinit var buffer: DataBuffer<TemperaturePoint>

    init {
        view = viewTemperatureMain.panelLtuInnerCharts
        view.addListeners(this, BaseTemperatureLimitTracker(view, this))
        view.sourceChoose.addItem(getAvailablePorts().first())
        for (i in 0 until view.tempChTrackEns.size) {
            currentTemp.add(0.0)
        }
    }

    override fun consume(t: TemperaturePoint) {
        commonLogWriter.temperatureLtuInner = t
        super.consume(t)
    }

    override fun startReading() {
        sourceDevice.setReadEn(true)

        GlobalScope.launch {
            sourceDevice.startReading()
        }
    }

    override fun stopReading() {
        sourceDevice.setReadEn(false)
    }

    private fun getAvailablePorts():List<String> {
        return sourceDevice.getAvailable().toList()
    }

    override fun getDataBuffer(): DataBuffer<TemperaturePoint> {
        return buffer
    }
}