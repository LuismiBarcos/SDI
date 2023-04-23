package org.sdi.injector

import org.sdi.annotations.Component
import org.sdi.annotations.Inject
import java.lang.reflect.Field

/**
 *@author Luis Miguel Barcos
 */
class ComponentHandler {

    private val injector = Injector()

    fun handleClass(clazz: Class<*>) {
        if(clazz.isAnnotationPresent(Component::class.java)) {
            Context.applicationContext[clazz] = handleComponent(clazz)
        }
    }

    private fun handleComponent(clazz: Class<*>): Any {
        val newInstance = clazz.getDeclaredConstructor().newInstance()
        val clazzes = clazz.getAnnotation(Component::class.java).classes

        clazzes.forEach {
            fillDIContainer(newInstance, it.qualifiedName!!)
        }

        injector.handleInjects(newInstance, getClassFieldsAnnotatedWithInject(clazz, emptyList()))

        return newInstance
    }

    private fun fillDIContainer(instance: Any, classToInjectCanonicalName: String) {
        (Context.dIContainer[classToInjectCanonicalName]
            ?.let { it + instance }
            ?.toMutableList()
            ?: mutableListOf(instance))
            .also { Context.dIContainer[classToInjectCanonicalName] = it }
    }

    private tailrec fun getClassFieldsAnnotatedWithInject(clazz: Class<*>, fields: List<Field>): List<Field> =
        if(clazz.superclass == null) {
            fields
        } else {
            getClassFieldsAnnotatedWithInject(
                clazz.superclass,
                fields + clazz.declaredFields.filter {
                    it.isAnnotationPresent(Inject::class.java)
                })
        }
}