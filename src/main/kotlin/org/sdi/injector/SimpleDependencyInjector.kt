package org.sdi.injector

import org.sdi.annotations.Component
import org.sdi.annotations.Inject
import java.io.File
import java.lang.reflect.Field
import java.net.URI
import java.net.URL
import java.net.URLClassLoader
import java.util.jar.JarFile

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

        resources.forEach {
            if (it.protocol.equals("jar")) {
                loadJarClasses(URI(clazz.protectionDomain.codeSource.location.file).path, path)
            } else {
                val file = File(URI(it.file).path)
                loadFileClasses(file.listFiles(), packageName)
            }
        }

        executePendingInjections()
        clear()
    }

    private fun loadJarClasses(pathToJar: String, mainClassPath: String) {
        val entries = JarFile(pathToJar).entries()
        val urls: Array<URL> = arrayOf(URL("jar:file:$pathToJar!/"))
        val classLoader = URLClassLoader.newInstance(urls)

        for (entry in entries) {
            if (entry.isDirectory || !entry.name.startsWith(mainClassPath) || !entry.name.endsWith(".class")) {
                continue
            }
            val className = getClassName(entry.name).replace('/', '.')
            val clazz = classLoader.loadClass(className)
            if(clazz.isAnnotationPresent(Component::class.java)) {
                applicationContext[clazz] = handleComponent(clazz)
            }
        }
    }

    private fun loadFileClasses(files: Array<File>?, packageName: String) {
        files?.forEach {
            if (it.isDirectory) {
                loadFileClasses(it.listFiles(), "$packageName.${it.name}")
            } else {
                fillApplicationContext(Class.forName("$packageName.${getClassName(it.name)}"))
            }
        }
    }

    private fun getClassName(name: String): String = name.substring(0, name.length - ".class".length)

    private fun fillApplicationContext(clazz: Class<*>) {
        if(clazz.isAnnotationPresent(Component::class.java)) {
            applicationContext[clazz] = handleComponent(clazz)
        }
    }

    private fun handleComponent(clazz: Class<*>): Any {
        val newInstance = clazz.getDeclaredConstructor().newInstance()
        val clazzes = clazz.getAnnotation(Component::class.java).classes

        clazzes.forEach {
            fillDIContainer(newInstance, it.qualifiedName!!)
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
                injectDependency(instance, field)
            }
        }
    }

    private fun injectDependency(instance: Any, field: Field) {
        if (dIContainer.containsKey(field.type.canonicalName)) {
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
        if(applicationContext.containsKey(Class.forName(classToInjectCanonicalName))) {
            inject(instance, field, applicationContext[Class.forName(classToInjectCanonicalName)]!!)
        } else {
            pendingInjections.add(PendingInjection(instance, field))
        }
    }

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