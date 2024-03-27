package org.sdi.domain.usecases.helpers

import org.sdi.domain.model.Instance
import org.sdi.domain.model.PendingInjection
import org.sdi.domain.ports.ContextRepository

/**
 * @author Luis Miguel Barcos
 */
class Injector(private val contextRepository: ContextRepository) {
    fun injectSpecificDependency(classToInjectCanonicalName: String, pendingInjection: PendingInjection) {
        contextRepository.getClazzInstanceByCanonicalName(classToInjectCanonicalName)
            ?.let { clazzInstance -> inject(pendingInjection, clazzInstance) }
            ?: contextRepository.addPendingInjection(pendingInjection)
    }

    fun injectDependency(pendingInjection: PendingInjection) {
        TODO("Not yet implemented")
    }

    private fun inject(pendingInjection: PendingInjection, instanceToInject: Instance) {
        pendingInjection.field.value.isAccessible = true
        pendingInjection.field.value.set(pendingInjection.instance.value, instanceToInject.value)
        pendingInjection.field.value.isAccessible = false
    }

}
