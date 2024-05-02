package org.sdi.domain.usecases

import org.sdi.domain.ports.ContextRepository

/**
 * @author Luis Miguel Barcos
 */
class ClearContextHelpers(private val contextRepository: ContextRepository) {

    fun clear() {
        contextRepository.clearContextHelpers()
    }
}