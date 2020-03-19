package com.chilik1020.resourcekeeper.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.text.SimpleDateFormat



fun getDateTime(): String {
    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yy, HH:mm:ss")
    return current.format(formatter)
}

fun formatDateTime(date: Date): String {
    val pattern = "dd-MM-yy, HH:mm:ss"
    val simpleDateFormat = SimpleDateFormat(pattern)
    simpleDateFormat.format(date)
    return simpleDateFormat.format(date)
}

fun getDate():String {
    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("dd_MM_yy")
    return current.format(formatter)
}