package com.useraccount.services

/**
 *@author Luis Miguel Barcos
 */
interface AccountService {
    fun getAccountNumber(username: String): Long
}