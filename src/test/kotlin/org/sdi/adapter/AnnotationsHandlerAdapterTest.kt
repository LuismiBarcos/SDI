package org.sdi.adapter

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.sdi.domain.model.Clazz
import org.sdi.domain.model.Field
import org.sdi.helper.Foo
import kotlin.test.assertEquals

/**
 * @author Luis Miguel Barcos
 */
class AnnotationsHandlerAdapterTest {

    private lateinit var annotationsHandlerAdapter: AnnotationsHandlerAdapter

    @BeforeEach
    fun setUp() {
        annotationsHandlerAdapter = AnnotationsHandlerAdapter()
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
        val actualFields = annotationsHandlerAdapter.getFieldsMarkedWithInject(clazz)

        // then
        assertEquals(expectedFields, actualFields)
    }
}