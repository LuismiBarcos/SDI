package com.example.useraccount.services

import org.sdi.annotations.Component
import org.sdi.annotations.Inject

/**
 *@author Luis Miguel Barcos
 */
@Component
class UserAccountClient {
    @Inject
    private lateinit var userService: UserService

    @Inject("com.example.useraccount.services.impl.AccountServiceImpl")
    private lateinit var accountService: AccountService

    fun displayUserAccount() {
        val userName = userService.getUserName()
        val accountNumber = accountService.getAccountNumber(userName)
        println("Username: $userName\nAccountNumber: $accountNumber")
    }
}