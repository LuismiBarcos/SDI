package com.useraccount.services

import org.sdi.annotations.Component
import org.sdi.annotations.Inject

/**
 *@author Luis Miguel Barcos
 */
@Component
class UserAccountClient {
    @Inject
    private lateinit var userService: UserService

    @Inject
    private lateinit var accountService: AccountService

    fun displayUserAccount() {
        val userName = userService.getUserName()
        val accountNumber = accountService.getAccountNumber(userName)
        println("Username: $userName\nAccountNumber: $accountNumber")
    }
}