package com.chilik1020.resourcekeeper.base

interface Producer<T> {
    fun produce(t: T)

    fun startReading()

    fun setSource(source: String)

    fun getAvailable():Array<String>

    fun setReadEn(state: Boolean)
}