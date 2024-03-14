package org.sdi.domain

/**
 * @author Luis Miguel Barcos
 */
data class PendingInjection(
    val instance: Instance,
    val field: Field
)