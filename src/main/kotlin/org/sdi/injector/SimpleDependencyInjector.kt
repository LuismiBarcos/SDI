package org.sdi.injector

import org.sdi.adapter.InMemoryApplicationContextRepository
import org.sdi.domain.model.Clazz
import org.sdi.domain.usecases.*
import org.sdi.domain.usecases.helpers.AnnotationsHelper
import java.io.File
import java.net.URI
import java.net.URL
import java.net.URLClassLoader
import java.util.jar.JarFile

/**
 *@author Luis Miguel Barcos
 */
class SimpleDependencyInjector {
    // Dependencies
    private val annotationsHelper = AnnotationsHelper()
    private val contextRepository = InMemoryApplicationContextRepository()
    private val componentHandler = org.sdi.domain.usecases.helpers.ComponentHandler(
        contextRepository,
        annotationsHelper,
        org.sdi.domain.usecases.helpers.Injector(contextRepository)
    )

    // Use cases
    private val handlePendingInjection = HandlePendingInjection(componentHandler)
    private val clearContextHelpers = ClearContextHelpers(contextRepository)
    private val getPendingInjections = GetPendingInjections(contextRepository)
    private val getServiceByClass = GetServiceByClass(contextRepository)
    private val addToApplicationContext = AddToApplicationContext(
        annotationsHelper = annotationsHelper,
        contextRepository = contextRepository,
        componentHandler = componentHandler
    )

    fun <T> getService(clazz: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return getServiceByClass.get(Clazz(clazz)).value as T
    }

    fun init(clazz: Class<*>) {
        clazz.getResources().forEach {
            if (it.protocol.equals("jar")) {
                loadJarClasses(
                    URI(clazz.protectionDomain.codeSource.location.file).path,
                    clazz.getClassPackageNamePath()
                )
            } else {
                val file = File(URI(it.file).path)
                loadFileClasses(file.listFiles(), clazz.getClassPackageName())
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
            addToApplicationContext.add(Clazz(classLoader.loadClass(className)))
        }
    }

    private fun loadFileClasses(files: Array<File>?, packageName: String) {
        files?.forEach {
            if (it.isDirectory) {
                loadFileClasses(it.listFiles(), "$packageName.${it.name}")
            } else {
                addToApplicationContext.add(Clazz(Class.forName("$packageName.${getClassName(it.name)}")))
            }
        }
    }

    private fun getClassName(name: String): String = name.substring(0, name.length - ".class".length)

    private fun executePendingInjections() {
        getPendingInjections.get()
            .values()
            .forEach { handlePendingInjection.handle(it) }
    }

    private fun clear() {
        clearContextHelpers.clear()
    }
}