package org.sdi.domain.usecases

import org.sdi.domain.model.PendingInjection
import org.sdi.domain.usecases.helpers.ComponentHandler

/**
 * @author Luis Miguel Barcos
 */
class HandlePendingInjection(
    private val componentHandler: ComponentHandler
) {

    fun handle(pendingInjection: PendingInjection) {
        componentHandler.handlePendingInjection(pendingInjection)
    }
}