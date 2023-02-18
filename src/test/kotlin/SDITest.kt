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
        private lateinit var simpleDependencyInjector: SimpleDependencyInjector
        private lateinit var userAccountClient: UserAccountClient
    }

    @BeforeEach
    fun setup() {
        simpleDependencyInjector = SimpleDependencyInjector()
        simpleDependencyInjector.init(UserAccountClient::class.java)
        userAccountClient = simpleDependencyInjector.getService(UserAccountClient::class.java)
    }

    @Test
    fun `test get a component instance`() {
        val service = simpleDependencyInjector.getService(UserServiceImpl::class.java)
        assert(service.getUserName() == "username")
    }

    @Test
    fun `test get a main class instance of a project with all fields injected`() {
        val service = simpleDependencyInjector.getService(UserAccountClient::class.java)
        val fields = service.javaClass.declaredFields
        assert(fields.all { it != null })
    }
}