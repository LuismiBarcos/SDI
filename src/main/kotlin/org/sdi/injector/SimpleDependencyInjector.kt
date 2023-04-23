package org.sdi.injector

import java.io.File
import java.net.URI
import java.net.URL
import java.net.URLClassLoader
import java.util.jar.JarFile

/**
 *@author Luis Miguel Barcos
 */
class SimpleDependencyInjector {

    private val componentHandler = ComponentHandler()
    private val injector = Injector()

    fun <T> getService(clazz: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return Context.applicationContext[clazz] as T ?: throw Exception("Instance not found")
    }

    fun init(clazz: Class<*>) {
        clazz.getResources().forEach {
            if (it.protocol.equals("jar")) {
                loadJarClasses(URI(clazz.protectionDomain.codeSource.location.file).path, clazz.getClassPackageNamePath())
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
            componentHandler.handleClass(classLoader.loadClass(className))
        }
    }

    private fun loadFileClasses(files: Array<File>?, packageName: String) {
        files?.forEach {
            if (it.isDirectory) {
                loadFileClasses(it.listFiles(), "$packageName.${it.name}")
            } else {
                componentHandler.handleClass(Class.forName("$packageName.${getClassName(it.name)}"))
            }
        }
    }

    private fun getClassName(name: String): String = name.substring(0, name.length - ".class".length)

    private fun executePendingInjections() {
        Context.pendingInjections.forEach {
            injector.handleInjects(it.instance, listOf(it.field))
        }
    }

    private fun clear() {
        Context.dIContainer.clear()
        Context.pendingInjections.clear()
    }
}