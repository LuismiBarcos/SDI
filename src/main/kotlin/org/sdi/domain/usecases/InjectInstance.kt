package org.sdi.domain.usecases

import org.sdi.domain.model.Instance
import org.sdi.domain.model.PendingInjection
import org.sdi.domain.ports.Injector

/**
 * @author Luis Miguel Barcos
 */
class InjectInstance(val injector: Injector) {

    /**
     * Injects the provided instance into the pending injection, which has the instance and the field to update
     * @param pendingInjection
     * @param instanceToInject
     */
    fun inject(pendingInjection: PendingInjection, instanceToInject: Instance) {
        injector.inject(pendingInjection, instanceToInject)
    }
}