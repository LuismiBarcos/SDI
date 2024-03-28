package org.sdi.domain.usecases.helpers

import org.sdi.annotations.Component
import org.sdi.annotations.Inject
import org.sdi.domain.model.Clazz
import org.sdi.domain.model.Field

/**
 * @author Luis Miguel Barcos
 */
class AnnotationsHelper {

    fun isComponent(clazz: Clazz): Boolean = clazz.value.isAnnotationPresent(Component::class.java)

    fun getComponentClasses(component: Clazz): List<Clazz> =
        component.value.getAnnotation(Component::class.java)
            .classes
            .map { Clazz(it.java) }

    fun getInjectValue(field: Field): String = field.value.getAnnotation(Inject::class.java).value

    fun getFieldsMarkedWithInject(clazz: Clazz): List<Field> = getFieldsWithInject(clazz.value, emptyList())

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