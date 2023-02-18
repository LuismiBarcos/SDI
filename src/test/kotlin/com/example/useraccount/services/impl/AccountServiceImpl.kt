package com.example.useraccount.services.impl

import com.example.useraccount.services.AccountService
import org.sdi.annotations.Component

/**
 *@author Luis Miguel Barcos
 */
@Component
class AccountServiceImpl: AccountService {
    override fun getAccountNumber(username: String): Long = 123456789L
}