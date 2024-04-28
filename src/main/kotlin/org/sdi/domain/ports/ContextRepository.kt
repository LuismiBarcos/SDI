package org.sdi.domain.ports

import org.sdi.domain.model.ClassCanonicalName
import org.sdi.domain.model.Clazz
import org.sdi.domain.model.Instance
import org.sdi.domain.model.PendingInjection

/**
 * @author Luis Miguel Barcos
 */
interface ContextRepository {

    fun fillDIContainer(instance: Instance, clazz: Clazz)

    fun getClazzInstanceByCanonicalName(clazzCanonicalName: ClassCanonicalName): Instance?

    fun addPendingInjection(pendingInjection: PendingInjection)

    fun getClazzInstanceFromContainerByCanonicalName(canonicalName: ClassCanonicalName): Instance?

    fun addToApplicationContext(clazz: Clazz, instance: Instance)
}
