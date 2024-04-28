package org.sdi.domain.usecases

import com.example.useraccount.services.UserAccountClient
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.sdi.domain.model.Clazz
import org.sdi.domain.model.Instance
import org.sdi.domain.ports.ContextRepository
import org.sdi.domain.usecases.helpers.AnnotationsHelper
import org.sdi.domain.usecases.helpers.ComponentHandler

/**
 * @author Luis Miguel Barcos
 */
@ExtendWith(MockKExtension::class)
class AddToApplicationContextTest {

    @MockK(relaxUnitFun = true)
    private lateinit var componentHandler: ComponentHandler

    @MockK(relaxUnitFun = true)
    private lateinit var contextRepository: ContextRepository

    private lateinit var addToApplicationContext: AddToApplicationContext

    @BeforeEach
    fun setUp() {
        addToApplicationContext = AddToApplicationContext(AnnotationsHelper(), componentHandler, contextRepository)
    }

    @Test
    fun `Handle component class`() {
        // given
        val clazz = Clazz(UserAccountClient::class.java)
        val instance = aClazzInstance(clazz)
        every { componentHandler.handleFields(clazz, any()) } returns instance

        // when
        addToApplicationContext.add(clazz)

        // then
        verify(exactly = 1) { componentHandler.handleFields(clazz, any()) }
    }

    @Test
    fun `Do nothing when handling a non component class`() {
        // given
        val clazz = Clazz(String::class.java)
        val instance = aClazzInstance(clazz)

        // when
        addToApplicationContext.add(clazz)

        // then
        verify(exactly = 0) { componentHandler.handleFields(clazz, instance) }
    }

    @Test
    fun `Add component to application context`() {
        // given
        val clazz = Clazz(UserAccountClient::class.java)
        val instance = aClazzInstance(clazz)
        every { componentHandler.handleFields(clazz, any()) } returns instance

        // when
        addToApplicationContext.add(clazz)

        // then
        verify(exactly = 1) { componentHandler.handleClasses(clazz, any()) }
        verify(exactly = 1) { componentHandler.handleFields(clazz, any()) }
        verify(exactly = 1) { contextRepository.addToApplicationContext(clazz, any()) }
    }

    private fun aClazzInstance(clazz: Clazz) = Instance(clazz.value.getDeclaredConstructor().newInstance())
}