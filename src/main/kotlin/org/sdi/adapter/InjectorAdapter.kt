package org.sdi.adapter

import org.sdi.domain.Instance
import org.sdi.domain.PendingInjection
import org.sdi.usecases.ports.Injector

/**
 * @author Luis Miguel Barcos
 */
class InjectorAdapter : Injector {
    override fun inject(pendingInjection: PendingInjection, instanceToInject: Instance) {
        pendingInjection.field.value.isAccessible = true
        pendingInjection.field.value.set(pendingInjection.instance.value, instanceToInject.value)
        pendingInjection.field.value.isAccessible = false
    }
}