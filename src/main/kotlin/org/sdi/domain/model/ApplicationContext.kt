package org.sdi.domain.model

/**
 * @author Luis Miguel Barcos
 */
data class ApplicationContext(
    private var components: Components
) {
    fun addComponent(component: Component) {
        components = Components(components.values.plus(component))
    }

    fun getComponents() = components.copy(values = components.values)
}
