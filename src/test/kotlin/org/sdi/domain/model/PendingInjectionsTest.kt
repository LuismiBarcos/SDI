package org.sdi.domain.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.sdi.helper.Foo

/**
 * @author Luis Miguel Barcos
 */
class PendingInjectionsTest {

    private lateinit var pendingInjections: PendingInjections

    @BeforeEach
    fun setUp() {
        pendingInjections = PendingInjections(emptyList())
    }

    @Test
    fun `Add a pending injection`() {
        // given
        assertThat(pendingInjections.values()).hasSize(0)
        val pendingInjection = PendingInjection(Instance("string instance"), Field(Foo::class.java.declaredFields[0]))

        // when
        pendingInjections.addPendingInjection(pendingInjection)

        // then
        assertThat(pendingInjections.values()).hasSize(1)
    }
}