package org.sdi.domain.usecases

import org.sdi.annotations.Component
import org.sdi.domain.model.Clazz
import org.sdi.domain.model.Instance
import org.sdi.domain.ports.ContextRepository
import org.sdi.domain.usecases.helpers.AnnotationsHelper
import org.sdi.domain.usecases.helpers.ComponentHandler

/**
 * @author Luis Miguel Barcos
 */
class AddToApplicationContext(
    private val annotationsHelper: AnnotationsHelper,
    private val componentHandler: ComponentHandler,
    private val contextRepository: ContextRepository
) {

    /**
     * Adds the class to the application context in case the class is annotated with the [Component] annotation
     * @param clazz
     */
    fun add(clazz: Clazz) {
        if (annotationsHelper.isComponent(clazz)) {
            val componentInstance = Instance(clazz.value.getDeclaredConstructor().newInstance())
            componentHandler.handleClasses(clazz, componentInstance)
            componentHandler.handleFields(clazz, componentInstance)
            contextRepository.addToApplicationContext(clazz, componentInstance)
        }
    }
}