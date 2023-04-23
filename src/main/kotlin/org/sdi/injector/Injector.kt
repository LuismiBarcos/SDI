package org.sdi.injector

import org.sdi.annotations.Inject
import java.lang.reflect.Field

/**
 *@author Luis Miguel Barcos
 */
class Injector {
    fun handleInjects(instance:Any, fields: List<Field>) {
        fields.forEach { field ->
            val annotationValue = field.getAnnotation(Inject::class.java).value

            if(annotationValue.isNotBlank()) {
                injectSpecificDependency(annotationValue, instance, field)
            } else {
                injectDependency(instance, field)
            }
        }
    }

    private fun injectSpecificDependency(classToInjectCanonicalName: String, instance: Any, field: Field) {
        if(Context.applicationContext.containsKey(Class.forName(classToInjectCanonicalName))) {
            inject(instance, field, Context.applicationContext[Class.forName(classToInjectCanonicalName)]!!)
        } else {
            Context.pendingInjections.add(PendingInjection(instance, field))
        }
    }

    private fun injectDependency(instance: Any, field: Field) {
        if (Context.dIContainer.containsKey(field.type.canonicalName)) {
            inject(instance, field, getFirstImplamentation(field))
        } else {
            Context.pendingInjections.add(PendingInjection(instance, field))
        }
    }


    private fun inject(instance: Any, field: Field, bean: Any) {
        field.isAccessible = true
        field.set(instance, bean)
        field.isAccessible = false
    }

    private fun getFirstImplamentation(field: Field): Any =
        Context.dIContainer[field.type.canonicalName]
            ?.first()
            ?: throw Exception("Cannot find an implementation for $field")
}