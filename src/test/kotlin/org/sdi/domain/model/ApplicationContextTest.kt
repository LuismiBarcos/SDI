package org.sdi.domain.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * @author Luis Miguel Barcos
 */
class ApplicationContextTest {

    private lateinit var applicationContext: ApplicationContext

    @BeforeEach
    fun setUp() {
        applicationContext = ApplicationContext(Components(listOf()))
    }

    @Test
    fun `Application context returns components`() {
        // given
        applicationContext.addComponent(aComponent())

        // when
        val actualComponents = applicationContext.getComponents()

        // then
        assertThat(actualComponents).isEqualTo(expectedComponents())
    }

    @Test
    fun `Add component to application context`() {
        // given
        val expectedComponents = expectedComponents()
        assertThat(applicationContext.getComponents().values).hasSize(0)

        // when
        applicationContext.addComponent(aComponent())

        // then
        assertThat(applicationContext.getComponents().values).hasSize(1)
        assertThat(applicationContext.getComponents()).isEqualTo(expectedComponents)
    }

    private fun expectedComponents() =
        Components(listOf(aComponent()))

    private fun aComponent() = Component(
        Clazz(String::class.java),
        Implementation(Instance("String value"))
    )
}