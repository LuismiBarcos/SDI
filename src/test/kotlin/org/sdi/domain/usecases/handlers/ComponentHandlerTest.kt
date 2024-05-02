package org.sdi.domain.usecases.handlers

import com.example.useraccount.services.UserAccountClient
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.sdi.domain.model.*
import org.sdi.domain.ports.ContextRepository
import org.sdi.domain.usecases.helpers.AnnotationsHelper
import org.sdi.domain.usecases.helpers.ComponentHandler
import org.sdi.domain.usecases.helpers.Injector

/**
 * @author Luis Miguel Barcos
 */
@ExtendWith(MockKExtension::class)
class ComponentHandlerTest {

    @MockK(relaxUnitFun = true)
    private lateinit var injector: Injector

    @MockK(relaxUnitFun = true)
    private lateinit var annotationsHelper: AnnotationsHelper

    @MockK(relaxUnitFun = true)
    private lateinit var contextRepository: ContextRepository

    private lateinit var componentHandler: ComponentHandler

    @BeforeEach
    fun setUp() {
        componentHandler = ComponentHandler(contextRepository, annotationsHelper, injector)
    }

    @Test
    fun `Component classes are added to container`() {
        // given
        val clazz = Clazz(UserAccountClient::class.java)
        anInjectedFields(clazz)
        val instance = aClazzInstance(clazz)
        every { annotationsHelper.getComponentClasses(clazz) } returns listOf(Clazz(UserAccountClient::class.java))

        // when
        componentHandler.handleClasses(clazz, instance)

        // then
        verify(exactly = 1) { contextRepository.fillDIContainer(any(), clazz) }
    }

    @Test
    fun `Component injected fields are handled`() {
        // given
        val clazz = Clazz(UserAccountClient::class.java)
        anInjectedFields(clazz)
        val instance = aClazzInstance(clazz)

        // when
        componentHandler.handleFields(clazz, instance)

        // then
        verifyOrder {
            annotationsHelper.getFieldsMarkedWithInject(clazz)
            injector.injectDependency(any())
            injector.injectSpecificDependency(
                ClassCanonicalName("com.example.useraccount.services.impl.AccountServiceImpl"),
                any()
            )
        }
    }

    @Test
    fun `Handle pending injection`() {
        // given
        val clazz = Clazz(UserAccountClient::class.java)
        anInjectedFields(clazz)
        val aClazzInstance = aClazzInstance(clazz)
        val pendingInjection1 = PendingInjection(aClazzInstance, Field(UserAccountClient::class.java.declaredFields[0]))
        val pendingInjection2 = PendingInjection(aClazzInstance, Field(UserAccountClient::class.java.declaredFields[1]))
        val pendingInjections = PendingInjections(listOf(pendingInjection1, pendingInjection2))


        // when
        pendingInjections.values().forEach {
            componentHandler.handlePendingInjection(it)
        }

        // then
        verifyOrder {
            annotationsHelper.getInjectValue(pendingInjection1.field)
            injector.injectDependency(pendingInjection1)
            annotationsHelper.getInjectValue(pendingInjection2.field)
            injector.injectSpecificDependency(
                ClassCanonicalName("com.example.useraccount.services.impl.AccountServiceImpl"),
                pendingInjection2
            )
        }
    }

    private fun aClazzInstance(clazz: Clazz) = Instance(clazz.value.getDeclaredConstructor().newInstance())

    private fun anInjectedFields(clazz: Clazz) {
        val field1 = Field(clazz.value.declaredFields[0])
        val field2 = Field(clazz.value.declaredFields[1])
        every { annotationsHelper.getFieldsMarkedWithInject(clazz) } returns listOf(field1, field2)
        every { annotationsHelper.getInjectValue(field1) } returns ""
        every { annotationsHelper.getInjectValue(field2) } returns "com.example.useraccount.services.impl.AccountServiceImpl"
    }

}