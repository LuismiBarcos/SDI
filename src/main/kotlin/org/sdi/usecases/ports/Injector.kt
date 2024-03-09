package org.sdi.usecases.ports

import org.sdi.domain.Instance
import org.sdi.domain.PendingInjection

/**
 * @author Luis Miguel Barcos
 */
interface Injector {
    /**
     * Injects the provided instance into the pending injection, which has the instance and the field to update
     * pending injection.
     * @param pendingInjection
     * @param instanceToInject
     */
    fun inject(pendingInjection: PendingInjection, instanceToInject: Instance)
}