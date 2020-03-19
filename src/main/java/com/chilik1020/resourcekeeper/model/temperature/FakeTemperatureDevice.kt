package com.chilik1020.resourcekeeper.model.temperature

import com.chilik1020.resourcekeeper.base.Producer
import com.chilik1020.resourcekeeper.model.DataBuffer
import com.chilik1020.resourcekeeper.model.data.TemperaturePoint
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

class FakeTemperatureDevice(private val buffer: DataBuffer<TemperaturePoint>): Producer<TemperaturePoint> {

    private var readEn = false

    private val list = mutableListOf<Double>()
    private val listMeanValues = mutableListOf<Double>()

    init {
        for (i in 0..7)
            list.add(0.0)

        listMeanValues.add(0.0)
        listMeanValues.add(6.3)
        listMeanValues.add(5.8)
        listMeanValues.add(18.9)
        listMeanValues.add(11.3)
        listMeanValues.add(8.9)
        listMeanValues.add(0.0)
        listMeanValues.add(0.0)
    }

    override fun startReading() {
        while (readEn) {
            val r = Random()
            for (i in 0..7) {
                val mean = listMeanValues[i]
                val min = mean - 0.15
                val max = mean + 0.15
                val random = min + r.nextDouble() * (max - min)

                list[i] = BigDecimal(random).setScale(1, RoundingMode.HALF_EVEN).toDouble()
            }
            val listTemp: List<Double> = ArrayList<Double>(list)
            val tp = TemperaturePoint(Date(), listTemp)
            produce(tp)
            Thread.sleep(1000)
        }
    }

    override fun produce(t: TemperaturePoint) {
        buffer.add(t)
    }

    override fun setSource(source: String) {

    }

    override fun getAvailable(): Array<String> {
        return arrayOf("Fake Com1", "Fake Com2")
    }

    override fun setReadEn(state: Boolean) {
        this.readEn = state
    }

}