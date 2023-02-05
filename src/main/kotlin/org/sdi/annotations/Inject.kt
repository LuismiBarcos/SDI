package org.sdi.annotations

/**
 *@author Luis Miguel Barcos
 */
@Target(AnnotationTarget.CONSTRUCTOR, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Inject(val value: String = "")
