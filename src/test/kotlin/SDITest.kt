import com.useraccount.services.UserAccountClient
import com.useraccount.services.impl.UserServiceImpl
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.sdi.injector.Injector

/**
 *@author Luis Miguel Barcos
 */
class SDITest {

    companion object {
        private lateinit var injector: Injector
        private lateinit var userAccountClient: UserAccountClient
    }

    @BeforeEach
    fun setup() {
        injector = Injector()
        injector.initSDI(UserAccountClient::class.java.`package`.name)
        userAccountClient = injector.getService(UserAccountClient::class.java)
    }

    @Test
    fun test() {
        val service = injector.getService(UserServiceImpl::class.java)
        assert(service.getUserName() == "username")
    }
}