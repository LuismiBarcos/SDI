package org.sdi.domain.model

/**
 * @author Luis Miguel Barcos
 */
data class PendingInjection(
    val instance: Instance,
    val field: Field
)