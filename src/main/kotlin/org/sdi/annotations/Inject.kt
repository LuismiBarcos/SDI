package org.sdi.annotations

import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.*

/**
 * @author Luis Miguel Barcos
 */
@Retention(RUNTIME)
@Target(CONSTRUCTOR, FIELD)
@MustBeDocumented
annotation class Inject(val value: String = "")
