package org.sdi.domain

/**
 * @author Luis Miguel Barcos
 */
data class Container(
    val clazz: ClassCanonicalName,
    val implementations: Implementations
)