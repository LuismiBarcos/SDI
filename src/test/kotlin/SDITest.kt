import com.example.animals.AnimalsClient
import com.example.useraccount.services.UserAccountClient
import com.example.useraccount.services.impl.UserServiceImpl
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.sdi.injector.SimpleDependencyInjector

/**
 *@author Luis Miguel Barcos
 */
class SDITest {

    companion object {
        private lateinit var userApplicationInjector: SimpleDependencyInjector
        private lateinit var animalsApplicationInjector: SimpleDependencyInjector
    }

    @BeforeEach
    fun setup() {
        userApplicationInjector = SimpleDependencyInjector()
        userApplicationInjector.init(UserAccountClient::class.java)

        animalsApplicationInjector = SimpleDependencyInjector()
        animalsApplicationInjector.init(AnimalsClient::class.java)
    }

    @Test
    fun `test get a component instance`() {
        val service = userApplicationInjector.getService(UserServiceImpl::class.java)
        assert(service.getUserName() == "username")
    }

    @Test
    fun `test get a main class instance of user account client with all fields injected`() {
        val service = userApplicationInjector.getService(UserAccountClient::class.java)
        val fields = service.javaClass.declaredFields
        assert(fields.all { it != null })
    }

    @Test
    fun `test get a main class instance of a animal client with all fields injected`() {
        val service = animalsApplicationInjector.getService(AnimalsClient::class.java)
        val fields = service.javaClass.declaredFields
        assert(fields.all { it != null })
    }
}