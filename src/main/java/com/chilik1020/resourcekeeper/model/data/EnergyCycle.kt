package com.chilik1020.resourcekeeper.model.data


data class EnergyCycle(
        val date: String,
        val currentShot: Long,
        var fullResource: Long,
        val startEnergy: Double,
        val stopEnergy: Double,
        val meanEnergy: Double
)