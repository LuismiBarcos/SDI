package org.sdi.adapter

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.sdi.domain.model.*
import org.sdi.helper.Foo

/**
 * @author Luis Miguel Barcos
 */
class InMemoryApplicationContextRepositoryTest {

    private lateinit var repository: InMemoryApplicationContextRepository

    @BeforeEach
    fun setUp() {
        repository = InMemoryApplicationContextRepository()
    }

    @Test
    fun `Fill DI container`() {
        // given
        val instance = Instance(Foo())
        val clazz = Clazz(Foo::class.java)
        val container = InMemoryApplicationContextRepository.Context.container
        assertThat(container.values()).isEmpty()

        // when
        repository.fillDIContainer(instance, clazz)

        // then
        assertThat(container.values()).hasSize(1)
    }
}