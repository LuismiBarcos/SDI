package com.example.useraccount.services.impl

import com.example.useraccount.services.UserService
import org.sdi.annotations.Component

/**
 *@author Luis Miguel Barcos
 */
@Component
class UserServiceImpl: UserService {
    override fun getUserName(): String = "username"
}