package org.sdi.domain.usecases.helpers

import org.sdi.domain.model.*
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
        annotationsHelper.getComponentClasses(clazz)
            .forEach { contextRepository.fillDIContainer(componentInstance, Clazz(it.value)) }
    }

    fun handleFields(clazz: Clazz, componentInstance: Instance) {
        annotationsHelper
            .getFieldsMarkedWithInject(clazz)
            .forEach { injectField(it, componentInstance) }
    }

    private fun injectField(field: Field, componentInstance: Instance) {
        val classCanonicalName = ClassCanonicalName(annotationsHelper.getInjectValue(field))
        if (classCanonicalName.value.isNotBlank()) {
            injector.injectSpecificDependency(classCanonicalName, PendingInjection(componentInstance, field))
        } else {
            injector.injectDependency(PendingInjection(componentInstance, field))
        }
    }
}
