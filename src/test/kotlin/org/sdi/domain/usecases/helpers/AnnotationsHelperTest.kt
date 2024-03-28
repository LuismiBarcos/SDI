package org.sdi.domain.usecases.helpers

import com.example.useraccount.services.UserAccountClient
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.sdi.domain.model.Clazz
import org.sdi.domain.model.Field
import org.sdi.helper.Foo

/**
 * @author Luis Miguel Barcos
 */
class AnnotationsHelperTest {
    private lateinit var annotationsHelper: AnnotationsHelper

    @BeforeEach
    fun setUp() {
        annotationsHelper = AnnotationsHelper()
    }

    @Test
    fun `Return true if a class is a component`() {
        // given
        val componentClazz = Clazz(UserAccountClient::class.java)
        val nonComponentClazz = Clazz(String::class.java)

        // then
        assertThat(annotationsHelper.isComponent(componentClazz)).isTrue()
        assertThat(annotationsHelper.isComponent(nonComponentClazz)).isFalse()
    }

    @Test
    fun `Get fields with inject annotation`() {
        // given
        val expectedFields = listOf(
            Field(Foo::class.java.declaredFields[1]),
            Field(Foo::class.java.declaredFields[2])
        )
        val clazz = Clazz(Foo::class.java)

        // when
        val actualFields = annotationsHelper.getFieldsMarkedWithInject(clazz)

        // then
        assertThat(actualFields).isEqualTo(expectedFields)
    }
}