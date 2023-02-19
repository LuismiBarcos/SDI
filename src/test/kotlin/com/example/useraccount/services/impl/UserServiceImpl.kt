package com.example.useraccount.services.impl

import com.example.useraccount.services.UserService
import org.sdi.annotations.Component

/**
 *@author Luis Miguel Barcos
 */
@Component(["com.example.useraccount.services.UserService"])
class UserServiceImpl: UserService {
    override fun getUserName(): String = "username"
}