package com.example.useraccount.services

import org.sdi.domain.annotations.Component
import org.sdi.domain.annotations.Inject

/**
 *@author Luis Miguel Barcos
 */
@Component(classes = [UserAccountClient::class])
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