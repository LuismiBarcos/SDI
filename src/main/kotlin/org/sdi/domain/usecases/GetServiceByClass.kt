package org.sdi.domain.usecases

import org.sdi.domain.model.Clazz
import org.sdi.domain.model.Instance
import org.sdi.domain.ports.ContextRepository

/**
 * @author Luis Miguel Barcos
 */
class GetServiceByClass(private val contextRepository: ContextRepository) {

    fun get(clazz: Clazz): Instance =
        contextRepository.getClazzInstanceByCanonicalName(clazz.getCanonicalName())
            ?: throw Exception("Instance not found")
}