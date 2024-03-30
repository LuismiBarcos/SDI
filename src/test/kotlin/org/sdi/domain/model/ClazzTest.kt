package org.sdi.domain.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.sdi.helper.Foo

/**
 * @author Luis Miguel Barcos
 */
class ClazzTest {
    private lateinit var clazz: Clazz

    @Test
    fun `Get canonical name`() {
        // given
        clazz = Clazz(Foo::class.java)

        // when
        val canonicalName = clazz.getCanonicalName()

        // then
        assertThat(canonicalName).isEqualTo(ClassCanonicalName(Foo::class.java.canonicalName))
    }
}