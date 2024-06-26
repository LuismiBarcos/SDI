package com.example.useraccount.services

import org.sdi.injector.SimpleDependencyInjector


/**
 *@author Luis Miguel Barcos
 */
private class Main

fun main() {
    val injector = SimpleDependencyInjector()
    injector.init(Main::class.java)

    val userAccountClient = injector.getService(UserAccountClient::class.java)
    userAccountClient.displayUserAccount()
}