package org.sdi.domain.usecases.helpers

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.sdi.domain.model.ClassCanonicalName
import org.sdi.domain.model.Field
import org.sdi.domain.model.Instance
import org.sdi.domain.model.PendingInjection
import org.sdi.domain.ports.ContextRepository
import org.sdi.helper.Foo
import kotlin.test.assertFailsWith

/**
 * @author Luis Miguel Barcos
 */
@ExtendWith(MockKExtension::class)
class InjectorTest {

    @MockK(relaxUnitFun = true)
    private lateinit var contextRepository: ContextRepository

    private lateinit var injector: Injector

    @BeforeEach
    fun setUp() {
        injector = Injector(contextRepository)
    }

    @Test
    fun `Inject specific dependency`() {
        // given
        val component = Foo()
        val myInstanceField = Field(Foo::class.java.declaredFields[0])
        val expectedValue = Instance("myValue")
        val canonicalName = ClassCanonicalName(String::class.java.canonicalName)

        every { contextRepository.getClazzInstanceByCanonicalName(canonicalName) } returns expectedValue

        // when
        injector.injectSpecificDependency(canonicalName, PendingInjection(Instance(component), myInstanceField))

        // then
        assertThat(component.getName()).isEqualTo(expectedValue.value)
    }

    @Test
    fun `Add as pending injection when no instance for canonical name`() {
        // given
        val component = Foo()
        val canonicalName = ClassCanonicalName(String::class.java.canonicalName)
        val pendingInjection = PendingInjection(Instance(component), Field(Foo::class.java.declaredFields[0]))

        every { contextRepository.getClazzInstanceByCanonicalName(canonicalName) } returns null

        // when
        injector.injectSpecificDependency(canonicalName, pendingInjection)

        // then
        assertFailsWith<UninitializedPropertyAccessException> { component.getName() }
        verify(exactly = 1) { contextRepository.addPendingInjection(pendingInjection) }
    }

    @Test
    fun `Inject dependency`() {
        // given
        val component = Foo()
        val expectedValue = Instance("myValue")
        val canonicalName = ClassCanonicalName(String::class.java.canonicalName)

        every { contextRepository.getClazzInstanceFromContainerByCanonicalName(canonicalName) } returns expectedValue

        // when
        injector.injectDependency(PendingInjection(Instance(component), Field(Foo::class.java.declaredFields[0])))

        // then
        assertThat(component.getName()).isEqualTo(expectedValue.value)
    }

    @Test
    fun `Add as pending injection when no instance found`() {
        // given
        val component = Foo()
        val canonicalName = ClassCanonicalName(String::class.java.canonicalName)
        val pendingInjection = PendingInjection(Instance(component), Field(Foo::class.java.declaredFields[0]))

        every { contextRepository.getClazzInstanceFromContainerByCanonicalName(canonicalName) } returns null

        // when
        injector.injectDependency(pendingInjection)

        // then
        assertFailsWith<UninitializedPropertyAccessException> { component.getName() }
        verify(exactly = 1) { contextRepository.addPendingInjection(pendingInjection) }
    }
}