package org.sdi.adapter

import org.sdi.domain.model.*
import org.sdi.domain.ports.ContextRepository

/**
 * @author Luis Miguel Barcos
 */
class InMemoryApplicationContextRepository : ContextRepository {
    internal object Context {
        val container = Container()
        val applicationContext = ApplicationContext(Components(emptyList()))
    }

    override fun fillDIContainer(instance: Instance, clazz: Clazz) {
        Context.container.addImplementation(clazz.getCanonicalName(), Implementation(instance))
    }

    override fun getClazzInstanceByCanonicalName(clazzCanonicalName: ClassCanonicalName): Instance? {
        TODO("Not yet implemented")
    }

    override fun addPendingInjection(pendingInjection: PendingInjection) {
        TODO("Not yet implemented")
    }

    override fun getClazzInstanceFromContainerByCanonicalName(canonicalName: ClassCanonicalName): Instance? {
        TODO("Not yet implemented")
    }

    override fun addToApplicationContext(clazz: Clazz, instance: Instance) {
        Context.applicationContext.addComponent(Component(clazz, Implementation(instance)))
    }
}