package com.chilik1020.resourcekeeper.model.data

import javax.swing.JPanel

data class AlertMessage(
        val type: AlertLevel,
        val message: String,
        val view: JPanel
)