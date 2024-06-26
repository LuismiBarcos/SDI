package com.example.useraccount.services.impl

import com.example.useraccount.services.AccountService
import org.sdi.domain.annotations.Component

/**
 *@author Luis Miguel Barcos
 */
@Component(classes = [AccountService::class])
class AccountServiceImpl: AccountService {
    override fun getAccountNumber(username: String): Long = 123456789L
}