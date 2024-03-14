package org.sdi.domain.usecases

import org.sdi.domain.model.Clazz
import org.sdi.domain.model.Field
import org.sdi.domain.ports.AnnotationsHandler

/**
 * @author Luis Miguel Barcos
 */
class GetClassFieldsAnnotatedWithInject(private val annotationsHandler: AnnotationsHandler) {
    /**
     * Extract the fields that are marked with the [org.sdi.annotations.Inject] annotation
     *
     */
    fun getFields(clazz: Clazz): List<Field> = annotationsHandler.getFieldsMarkedWithInject(clazz)
}