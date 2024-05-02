package org.sdi.domain.usecases

import org.sdi.domain.model.PendingInjections
import org.sdi.domain.ports.ContextRepository

/**
 * @author Luis Miguel Barcos
 */
class GetPendingInjections(private val contextRepository: ContextRepository) {

    fun get(): PendingInjections = contextRepository.getPendingInjections()
}