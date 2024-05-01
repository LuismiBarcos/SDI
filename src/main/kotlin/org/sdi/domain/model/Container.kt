package org.sdi.domain.model

/**
 * @author Luis Miguel Barcos
 */
data class Container(
    private var value: Map<ClassCanonicalName, Implementations> = emptyMap(),
) {
    fun addImplementation(canonicalName: ClassCanonicalName, implementation: Implementation) {
        value[canonicalName]
            ?.let { it.values += implementation }
            ?: Implementations(listOf(implementation)).also { value = value + mapOf(Pair(canonicalName, it)) }
    }

    fun getImplementations(canonicalName: ClassCanonicalName): Implementations? = value[canonicalName]

    fun values() = value.toMap()
}