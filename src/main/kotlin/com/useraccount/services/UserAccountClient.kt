package com.useraccount.services

import org.sdi.annotations.Component

/**
 *@author Luis Miguel Barcos
 */
@Component
class UserAccountClient(private val userService: UserService, private val accountService: AccountService) {
    fun displayUserAccount() {
        val userName = userService.getUserName()
        val accountNumber = accountService.getAccountNumber(userName)
        println("Username: $userName\nAccountNumber: $accountNumber")
    }
}