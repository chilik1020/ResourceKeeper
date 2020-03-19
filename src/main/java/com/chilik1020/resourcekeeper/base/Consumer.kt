package com.chilik1020.resourcekeeper.base

interface Consumer<T> {

    fun consume(t: T)

    fun onDestroyConsumer()
}