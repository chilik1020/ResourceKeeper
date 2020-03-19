package com.chilik1020.resourcekeeper.model.log

import com.chilik1020.resourcekeeper.model.DataBuffer
import com.chilik1020.resourcekeeper.utils.JsonConfig
import java.io.File
import java.io.RandomAccessFile

abstract class LogReader<T>(val buffer: DataBuffer<T>){

    private lateinit var file: File
    lateinit var logPath: String
    var readEn: Boolean = false

    private var lastReadLine: String = ""

    private val FREQ1 = 1L
    private val FREQ2 = JsonConfig.periodShotMs
    private var readFreqMs: Long = 0L

    fun startReading() {

        file = File(logPath)

        val raf = RandomAccessFile(file,"r")
        val lines = file.readLines()
        val currentPosition = raf.length()

        lines.forEach {
            parseLineAndProduce(it)
        }
        raf.seek(currentPosition)

        readFreqMs = FREQ1

        while (readEn) {

            val line = raf.readLine()
            when (line) {
                lastReadLine -> {}
                null -> readFreqMs = FREQ2
                else -> {
                    lastReadLine = line
                    parseLineAndProduce(line)
                }
            }

            Thread.sleep(readFreqMs)
        }
    }

    abstract fun parseLineAndProduce(line: String)

    fun produce(t: T) {
        buffer.add(t)
    }
}