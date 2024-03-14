package org.sdi.adapter

import org.sdi.annotations.Inject
import org.sdi.domain.model.Clazz
import org.sdi.domain.model.Field
import org.sdi.domain.ports.AnnotationsHandler

/**
 * @author Luis Miguel Barcos
 */
class AnnotationsHandlerAdapter : AnnotationsHandler {
    override fun getFieldsMarkedWithInject(clazz: Clazz): List<Field> = getFieldsWithInject(clazz.value, emptyList())

    private tailrec fun getFieldsWithInject(clazz: Class<*>, fields: List<Field>): List<Field> =
        if (clazz.superclass == null) {
            fields
        } else {
            getFieldsWithInject(
                clazz.superclass,
                fields + getClazzFieldsWithInjectAnnotation(clazz)
            )
        }

    private fun getClazzFieldsWithInjectAnnotation(clazz: Class<*>): List<Field> =
        clazz.declaredFields
            .filter { it.isAnnotationPresent(Inject::class.java) }
            .map(::toField)

    private fun toField(field: java.lang.reflect.Field): Field = Field(field)
}