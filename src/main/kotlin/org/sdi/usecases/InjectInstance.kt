package org.sdi.usecases

import org.sdi.domain.Instance
import org.sdi.domain.PendingInjection
import org.sdi.usecases.ports.Injector

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