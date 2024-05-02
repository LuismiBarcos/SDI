package org.sdi.domain.model

/**
 * @author Luis Miguel Barcos
 */
data class PendingInjections(
    private var values: List<PendingInjection>
) {
    fun addPendingInjection(pendingInjection: PendingInjection) {
        values += pendingInjection
    }

    fun values(): List<PendingInjection> = values.toList()
}
