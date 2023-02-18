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
    private val pendingInjections = mutableListOf<PendingInjection>()

    fun getService(clazz: Class<*>): Any = applicationContext[clazz]!!

    fun initSDI(packageName: String) {
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

        if(interfaces.isEmpty())
            dIContainer[newInstance.javaClass.canonicalName] = newInstance
        else {
            interfaces.forEach {
                dIContainer[it.canonicalName] = newInstance
            }
        }

        handleInjects(newInstance, getClassFieldsAnnotatedWithInject(clazz, emptyList()))

        return newInstance
    }

    private fun handleInjects(instance:Any, fields: List<Field>) {
        fields.forEach {
            val classToInjectCanonicalName = it.type.canonicalName

            if (dIContainer.containsKey(classToInjectCanonicalName)) {
                inject(instance, it)
            } else {
                pendingInjections.add(PendingInjection(instance, it))
            }
        }
    }

    private fun inject(instance: Any, field: Field) {
        field.isAccessible = true
        field.set(instance, dIContainer[field.type.canonicalName])
        field.isAccessible = false
    }

    private fun executePendingInjections() {
        pendingInjections.forEach {
            inject(it.instance, it.field)
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

    private data class PendingInjection(
        val instance: Any,
        val field: Field,
    )
}