package org.sdi.domain.usecases.helpers

import org.sdi.domain.model.ClassCanonicalName
import org.sdi.domain.model.Instance
import org.sdi.domain.model.PendingInjection
import org.sdi.domain.ports.ContextRepository

/**
 * @author Luis Miguel Barcos
 */
class Injector(private val contextRepository: ContextRepository) {
    fun injectSpecificDependency(classToInjectCanonicalName: ClassCanonicalName, pendingInjection: PendingInjection) {
        contextRepository.getClazzInstanceByCanonicalName(classToInjectCanonicalName)
            ?.let { clazzInstance -> inject(pendingInjection, clazzInstance) }
            ?: contextRepository.addPendingInjection(pendingInjection)
    }

    fun injectDependency(pendingInjection: PendingInjection) {
        val fieldTypeCanonicalName = ClassCanonicalName(pendingInjection.field.value.type.canonicalName)
        contextRepository.getClazzInstanceFromContainerByCanonicalName(fieldTypeCanonicalName)
            ?.let { clazzInstance -> inject(pendingInjection, clazzInstance) }
            ?: contextRepository.addPendingInjection(pendingInjection)
    }

    private fun inject(pendingInjection: PendingInjection, instanceToInject: Instance) {
        pendingInjection.field.value.isAccessible = true
        pendingInjection.field.value.set(pendingInjection.instance.value, instanceToInject.value)
        pendingInjection.field.value.isAccessible = false
    }

}
