package org.sdi.injector

/**
 *@author Luis Miguel Barcos
 */
object Context {
    internal val dIContainer = mutableMapOf<String, MutableList<Any>>()
    internal val applicationContext = mutableMapOf<Class<*>, Any>()
    internal val pendingInjections = mutableListOf<PendingInjection>()
}