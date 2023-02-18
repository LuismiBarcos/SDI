package com.example.useraccount.services

/**
 *@author Luis Miguel Barcos
 */
interface AccountService {
    fun getAccountNumber(username: String): Long
}