package org.sdi.domain.usecases.handlers

import com.example.useraccount.services.UserAccountClient
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import io.mockk.verifyOrder
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.sdi.domain.model.Clazz
import org.sdi.domain.model.Field
import org.sdi.domain.model.Instance
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
        val actualInstance = componentHandler.handleFields(clazz, instance)

        // then
        verifyOrder {
            annotationsHelper.getFieldsMarkedWithInject(clazz)
            injector.injectDependency(any())
            injector.injectSpecificDependency(
                "com.example.useraccount.services.impl.AccountServiceImpl",
                any()
            )
        }
        assertThat(actualInstance).isEqualTo(instance)
    }

    private fun aClazzInstance(clazz: Clazz) = Instance(clazz.value.getDeclaredConstructor().newInstance())

    private fun anInjectedFields(clazz: Clazz) {
        every { annotationsHelper.getFieldsMarkedWithInject(clazz) } returns listOf(
            Field(clazz.value.declaredFields[0]),
            Field(clazz.value.declaredFields[1])
        )
    }

}