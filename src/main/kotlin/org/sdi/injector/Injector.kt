package org.sdi.injector

import org.sdi.annotations.Component
import org.sdi.annotations.Inject
import java.io.File
import java.lang.reflect.Field
import java.net.URI


/**
 *@author Luis Miguel Barcos
 */
class Injector {

    private val dIContainer = mutableMapOf<String, Any>()
    private val applicationContext = mutableMapOf<Class<*>, Any>()

    public fun getService(clazz: Class<*>): Any = applicationContext[clazz]!!

    public fun getClasses(packageName: String): Iterable<Class<*>> {
        val classLoader = Thread.currentThread().contextClassLoader
        val path = packageName.replace('.', '/')
        val resources = classLoader.getResources(path).toList()

        return resources.map {
            File(URI(it.file).path)
        }.map {
            if (it.exists()) findClasses(it.listFiles(), packageName) else emptyList()
        }.flatten()
    }

    private fun findClasses(files: Array<File>?, packageName: String): List<Class<*>> =
        files?.map {
            if (it.isDirectory) {
                findClasses(it.listFiles(), "$packageName.${it.name}")
            } else {
                val clazz = Class.forName("$packageName.${it.name.substring(0, it.name.length - 6)}")
                if(clazz.isAnnotationPresent(Component::class.java)) {
                    handleComponent(clazz)
                }
//                fillDIContainer(clazz)
//                handleInjects(clazz)
                listOf(Class.forName("$packageName.${it.name.substring(0, it.name.length - 6)}"))
            }
        }?.flatten()
            ?: emptyList()

    private fun handleComponent(clazz: Class<*>) {
        val newInstance = clazz.newInstance()
        val interfaces = clazz.interfaces

        if(interfaces.isEmpty())
            dIContainer[newInstance.javaClass.canonicalName] = newInstance
        else {
            interfaces.forEach {
                dIContainer[it.canonicalName] = newInstance
            }
        }

        handleInjects(newInstance, getClassFieldsAnnotatedWithInject(clazz, emptyList()))
        applicationContext[clazz] = newInstance
    }

    private fun handleInjects(instance:Any, fields: List<Field>) {
        fields.forEach {
            it.isAccessible = true
            it.set(instance, dIContainer[it.type.canonicalName])
            it.isAccessible = false
        }
    }

    private fun fillDIContainer(clazz: Class<*>) {
        if(clazz.isAnnotationPresent(Component::class.java)) {
            val newInstance = clazz.newInstance()
            val interfaces = clazz.interfaces
            if (interfaces.isEmpty()) {
                dIContainer[newInstance.javaClass.canonicalName] = newInstance
            } else {
                interfaces.forEach {
                    dIContainer[it.canonicalName] = newInstance
                }
            }
        }
    }

    private fun handleInjects(clazz: Class<*>) {
        val classFieldsAnnotatedWithInject = getClassFieldsAnnotatedWithInject(clazz, emptyList())
        if(classFieldsAnnotatedWithInject.isNotEmpty()) {
            val newInstance = clazz.newInstance()
            classFieldsAnnotatedWithInject.forEach {
                it.getAnnotation(Inject::class.java).value
                if(it.type.isInterface) {
                    it.isAccessible = true
                    it.set(newInstance, dIContainer[it.type.canonicalName])
                    it.isAccessible = false
                    applicationContext[clazz] = newInstance
                }
            }
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
}