# SDI - Simple Dependency Injector
Simple Dependency Injector is a Dependency Injector built in Kotlin to be used in small projects.

### How to use it
There are two main annotations:
* Component: Used to annotate the classes that should be injected by the injector. This annotation should be used in implementation classes or in classes that the user wants to be instantiated by the injector (like a Bean)
* Inject: Used to annotate the classes to be injected by the injector. In the future, the annotation will also support providing the value of a specific implementation that wants to be injected.

### Example
Imagine a project with the following structure
* Interfaces: 
  * AccountService
  * UserService
* Implementations: 
  * AccountServiceImpl
  * UserServiceImpl
* Client:
 * UserAccountClient
* Main

Here it is the implementation in Kotlin
##### Interfaces
###### AccountService
```
package com.useraccount.services

interface AccountService {
    fun getAccountNumber(username: String): Long
}
```
###### UserService
```
package com.useraccount.services

interface UserService {
    fun getUserName(): String
}
```
##### Implementations
###### AccountServiceImpl
```
package com.useraccount.services.impl

import com.useraccount.services.AccountService
import org.sdi.annotations.Component

@Component
class AccountServiceImpl: AccountService {
    override fun getAccountNumber(username: String): Long = 123456789L
}
```
###### UserServiceImpl
```
package com.useraccount.services.impl

import com.useraccount.services.UserService
import org.sdi.annotations.Component

@Component
class UserServiceImpl: UserService {
    override fun getUserName(): String = "username"
}
```
Note the use of @Component annotation to mark that these classes will be injected as an implementation of the interface
##### Client
###### UserAccountClient
```
package com.useraccount.services

import org.sdi.annotations.Component
import org.sdi.annotations.Inject

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
```
Note the use of @Inject annotation to mark the fields that should be injected by the injector. The injector will provide an instance of the interface implementation.
##### Main
###### Main
```
package com.useraccount.services

import org.sdi.injector.Injector
import java.util.concurrent.TimeUnit

fun main() {
    val startTime = System.nanoTime()
    val injector = Injector()
    injector.getClasses("com")

    val userAccountClient = injector.getService(UserAccountClient::class.java) as UserAccountClient
    userAccountClient.displayUserAccount()
    val endTime = System.nanoTime() - startTime
    println("Execution in milliseconds: ${TimeUnit.MILLISECONDS.convert(endTime, TimeUnit.NANOSECONDS)}")
}
```
This is the way that the injector is used. It is a pretty early version and should evolve soon.
### Used tools
[gitignore.io](https://www.toptal.com/developers/gitignore/)

[Used blog reference](https://dev.to/jjbrt/how-to-create-your-own-dependency-injection-framework-in-java-4eaj)

[Kotlin](https://kotlinlang.org/)