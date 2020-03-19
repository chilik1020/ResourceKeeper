package com.chilik1020.resourcekeeper.utils

fun checkCRC(data: Array<Int>, crc: Int): Boolean {
    var crcSum = 0

    for (item in data) {
        crcSum = leftShift(crcSum)
        crcSum = (crcSum + item) and 0xFF
    }
    return crcSum == crc
}

fun leftShift(b: Int): Int {
    val x = (b shl   1) and  0xFF
    val y = (b shr 7) and 0xFF
    return x or y
}