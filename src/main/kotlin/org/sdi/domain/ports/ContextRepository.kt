package org.sdi.domain.ports

import org.sdi.domain.model.*

/**
 * @author Luis Miguel Barcos
 */
interface ContextRepository {

    fun fillDIContainer(instance: Instance, clazz: Clazz)

    fun getClazzInstanceByCanonicalName(clazzCanonicalName: ClassCanonicalName): Instance?

    fun addPendingInjection(pendingInjection: PendingInjection)

    fun getFirstClazzInstanceFromContainerByCanonicalName(canonicalName: ClassCanonicalName): Instance?

    fun addToApplicationContext(clazz: Clazz, instance: Instance)

    fun getPendingInjections(): PendingInjections
}
