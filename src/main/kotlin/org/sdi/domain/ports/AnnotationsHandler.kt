package org.sdi.domain.ports

import org.sdi.domain.model.Clazz
import org.sdi.domain.model.Field

/**
 * @author Luis Miguel Barcos
 */
interface AnnotationsHandler {

    fun getFieldsMarkedWithInject(clazz: Clazz): List<Field>
}