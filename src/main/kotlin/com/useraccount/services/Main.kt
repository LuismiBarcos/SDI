package com.useraccount.services

import java.io.File
import java.net.URI


/**
 *@author Luis Miguel Barcos
 */
fun main() {
    getClasses("com").forEach { println(it) }
}

private fun getClasses(packageName: String): Iterable<Class<*>> {
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
            listOf(Class.forName("$packageName.${it.name.substring(0, it.name.length - 6)}"))
        }
    }?.flatten()
        ?: emptyList()