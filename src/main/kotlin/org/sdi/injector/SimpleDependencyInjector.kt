package org.sdi.injector

import org.sdi.annotations.Component
import org.sdi.annotations.Inject
import java.io.File
import java.lang.reflect.Field
import java.net.URI


/**
 *@author Luis Miguel Barcos
 */
class SimpleDependencyInjector {

    private val dIContainer = mutableMapOf<String, MutableList<Any>>()
    private val applicationContext = mutableMapOf<Class<*>, Any>()
    private val pendingInjections = mutableListOf<PendingInjection>()

    fun <T> getService(clazz: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return applicationContext[clazz] as T ?: throw Exception("Instance not found")
    }

    fun init(clazz: Class<*>) {
        val packageName = clazz.`package`.name
        val classLoader = Thread.currentThread().contextClassLoader
        val path = packageName.replace('.', '/')
        val resources = classLoader.getResources(path).toList()

        resources.map {
            File(URI(it.file).path)
        }.forEach {
            if (it.exists()) {
                fillApplicationContext(it.listFiles(), packageName)
            }
        }

        executePendingInjections()
        clear()
    }

    private fun fillApplicationContext(files: Array<File>?, packageName: String) {
        files?.forEach {
            if (it.isDirectory) {
                fillApplicationContext(it.listFiles(), "$packageName.${it.name}")
            } else {
                val clazz = Class.forName("$packageName.${it.name.substring(0, it.name.length - 6)}")
                if (clazz.isAnnotationPresent(Component::class.java)) {
                    applicationContext[clazz] = handleComponent(clazz)
                }
            }
        }
    }

    private fun handleComponent(clazz: Class<*>): Any {
        val newInstance = clazz.newInstance()
        val interfaces = clazz.interfaces

        if(interfaces.isEmpty()) {
            fillDIContainer(newInstance, newInstance.javaClass.canonicalName)
        }
        else {
            interfaces.forEach {
                fillDIContainer(newInstance, it.canonicalName)
            }
        }

        handleInjects(newInstance, getClassFieldsAnnotatedWithInject(clazz, emptyList()))

        return newInstance
    }

    private fun fillDIContainer(instance: Any, classToInjectCanonicalName: String) {
        (dIContainer[classToInjectCanonicalName]
            ?.let { it + instance }
            ?.toMutableList()
            ?: mutableListOf(instance))
        .also { dIContainer[classToInjectCanonicalName] = it }
    }

    private fun handleInjects(instance:Any, fields: List<Field>) {
        fields.forEach { field ->
            val annotationValue = field.getAnnotation(Inject::class.java).value

            if(annotationValue.isNotBlank()) {
                injectSpecificDependency(annotationValue, instance, field)
            } else {
                injectDependency(field.type.canonicalName, instance, field)
            }
        }
    }

    private fun injectDependency(classToInjectCanonicalName: String, instance: Any, field: Field) {
        if (dIContainer.containsKey(classToInjectCanonicalName)) {
            inject(instance, field, getFirstBean(field))
        } else {
            pendingInjections.add(PendingInjection(instance, field))
        }
    }

    private fun getFirstBean(field: Field): Any =
        dIContainer[field.type.canonicalName]
            ?.first()
            ?: throw Exception("Cannot find an implementation for $field")

    private fun injectSpecificDependency(classToInjectCanonicalName: String, instance: Any, field: Field) {
        inject(instance, field, getBeanToInject(classToInjectCanonicalName, field))
    }

    private fun getBeanToInject(classToInjectCanonicalName: String, field: Field): Any =
        dIContainer[field.type.canonicalName]
            ?.firstOrNull { it::class.java.canonicalName.equals(classToInjectCanonicalName) }
            ?: throw Exception("Field ${field.name} of class ${field.declaringClass} cannot be initialized with $classToInjectCanonicalName")

    private fun inject(instance: Any, field: Field, bean: Any) {
        field.isAccessible = true
        field.set(instance, bean)
        field.isAccessible = false
    }

    private fun executePendingInjections() {
        pendingInjections.forEach {
            handleInjects(it.instance, listOf(it.field))
        }
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

    private fun clear() {
        dIContainer.clear()
        pendingInjections.clear()
    }
}