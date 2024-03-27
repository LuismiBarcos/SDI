package org.sdi.domain.usecases.helpers

import org.sdi.annotations.Component
import org.sdi.annotations.Inject
import org.sdi.domain.model.Clazz
import org.sdi.domain.model.Field
import org.sdi.domain.model.Instance
import org.sdi.domain.model.PendingInjection
import org.sdi.domain.ports.ContextRepository

/**
 * @author Luis Miguel Barcos
 */
class ComponentHandler(
    private val contextRepository: ContextRepository,
    private val annotationsHelper: AnnotationsHelper,
    private val injector: Injector,
) {

    fun handleClasses(clazz: Clazz, componentInstance: Instance) {
        clazz.value.getAnnotation(Component::class.java)
            .classes
            .forEach { contextRepository.fillDIContainer(componentInstance, Clazz(it.java)) }
    }

    fun handleFields(clazz: Clazz, componentInstance: Instance): Instance {
        annotationsHelper
            .getFieldsMarkedWithInject(clazz)
            .forEach { injectField(it, componentInstance) }

        return componentInstance
    }

    private fun injectField(field: Field, componentInstance: Instance) {
        val value = field.value.getAnnotation(Inject::class.java).value
        if (value.isNotBlank()) {
            injector.injectSpecificDependency(value, PendingInjection(componentInstance, field))
        } else {
            injector.injectDependency(PendingInjection(componentInstance, field))
        }
    }
}
