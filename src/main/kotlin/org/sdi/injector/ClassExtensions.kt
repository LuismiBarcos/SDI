package org.sdi.injector

import java.net.URL

/**
 *@author Luis Miguel Barcos
 */

fun Class<*>.getResources(): List<URL> =
    Thread.currentThread().contextClassLoader.getResources(this.getClassPackageNamePath()).toList()

fun Class<*>.getClassPackageNamePath(): String = this.getClassPackageName().replace('.', '/')

fun Class<*>.getClassPackageName(): String = this.`package`.name