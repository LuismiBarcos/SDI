package com.useraccount.services

import org.sdi.injector.Injector
import kotlin.system.measureTimeMillis


/**
 *@author Luis Miguel Barcos
 */
fun main() {
    val measureTimeMillis = measureTimeMillis {
        val injector = Injector()
        injector.initSDI("com")

        val userAccountClient = injector.getService(UserAccountClient::class.java) as UserAccountClient
        userAccountClient.displayUserAccount()
    }
    println("Execution in milliseconds: $measureTimeMillis")
}