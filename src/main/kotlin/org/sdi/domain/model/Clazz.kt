package org.sdi.domain.model

/**
 * @author Luis Miguel Barcos
 */
data class Clazz(
    val value: Class<*>,
) {
    fun getCanonicalName(): ClassCanonicalName = ClassCanonicalName(value.canonicalName)
}
