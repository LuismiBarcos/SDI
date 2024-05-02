package org.sdi.domain.annotations

import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.TYPE
import kotlin.reflect.KClass

/**
 * @author Luis Miguel Barcos
 */
@Retention(RUNTIME)
@Target(TYPE, AnnotationTarget.CLASS)
@MustBeDocumented
annotation class Component(val classes: Array<KClass<*>>)