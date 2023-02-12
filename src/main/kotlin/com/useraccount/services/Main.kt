package com.useraccount.services

import org.sdi.injector.Injector
import java.util.concurrent.TimeUnit


/**
 *@author Luis Miguel Barcos
 */
fun main() {
    val startTime = System.nanoTime()
    val injector = Injector()
    injector.getClasses("com")

    val userAccountClient = injector.getService(UserAccountClient::class.java) as UserAccountClient
    userAccountClient.displayUserAccount()
    val endTime = System.nanoTime() - startTime
    println("Execution in milliseconds: ${TimeUnit.MILLISECONDS.convert(endTime, TimeUnit.NANOSECONDS)}")
}