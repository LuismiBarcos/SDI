package org.sdi.adapter

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.sdi.domain.Field
import org.sdi.domain.Instance
import org.sdi.domain.PendingInjection
import org.sdi.helper.Foo
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * @author Luis Miguel Barcos
 */
class InjectorAdapterTest {

    private lateinit var injector: InjectorAdapter

    @BeforeEach
    fun setUp() {
        injector = InjectorAdapter()
    }

    @Test
    fun `Inject instance into field`() {
        // given
        val instanceValue = Foo()
        val instance = Instance(instanceValue)
        val instanceField = Field(Foo::class.java.declaredFields[0])
        val pendingInjection = PendingInjection(instance, instanceField)
        assertFailsWith<UninitializedPropertyAccessException> { instanceValue.getName() }

        // when
        injector.inject(pendingInjection, Instance("Injected name"))

        // then
        assertEquals("Injected name", instanceValue.getName())
    }
}