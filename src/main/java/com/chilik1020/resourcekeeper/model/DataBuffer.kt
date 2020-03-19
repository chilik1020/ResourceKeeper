package com.chilik1020.resourcekeeper.model

import java.util.concurrent.ConcurrentLinkedQueue

class DataBuffer<T>{
    private val buffer = ConcurrentLinkedQueue<T>()

    fun add(data: T) {
        buffer.add(data)
    }

    fun poll(): T {
        return buffer.poll()
    }

    fun getSize(): Int {
        return buffer.size
    }

    fun clear() {
        buffer.clear()
    }
}