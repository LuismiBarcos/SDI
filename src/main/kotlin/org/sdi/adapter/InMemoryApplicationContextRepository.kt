package org.sdi.adapter

import org.sdi.domain.model.*
import org.sdi.domain.ports.ContextRepository

/**
 * @author Luis Miguel Barcos
 */
class InMemoryApplicationContextRepository : ContextRepository {
    internal object Context {
        var container = Container()
        var applicationContext = ApplicationContext(Components(emptyList()))
        var pendingInjections = PendingInjections(emptyList())
    }

    override fun fillDIContainer(instance: Instance, clazz: Clazz) {
        Context.container.addImplementation(clazz.getCanonicalName(), Implementation(instance))
    }

    override fun getClazzInstanceByCanonicalName(clazzCanonicalName: ClassCanonicalName): Instance? =
        Context.applicationContext.getComponents().values
            .find { it.clazz.getCanonicalName() == clazzCanonicalName }
            ?.implementation?.instance

    override fun addPendingInjection(pendingInjection: PendingInjection) {
        Context.pendingInjections.addPendingInjection(pendingInjection)
    }

    override fun getFirstClazzInstanceFromContainerByCanonicalName(canonicalName: ClassCanonicalName): Instance? =
        Context.container
            .getImplementations(canonicalName)?.values
            ?.first()
            ?.instance

    override fun addToApplicationContext(clazz: Clazz, instance: Instance) {
        Context.applicationContext.addComponent(Component(clazz, Implementation(instance)))
    }
}