package com.example.useraccount.services.impl

import com.example.useraccount.services.UserService
import org.sdi.domain.annotations.Component

/**
 *@author Luis Miguel Barcos
 */
@Component(classes = [UserService::class])
class UserServiceImpl: UserService {
    override fun getUserName(): String = "username"
}