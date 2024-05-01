package org.sdi.adapter

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
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

    @AfterEach
    fun tearDown() {
        clearContext()
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

    @Test
    fun `Add clazz and instance to application context`() {
        // given
        val clazz = Clazz(Foo::class.java)
        val clazzInstance = Instance(clazz.value.getDeclaredConstructor().newInstance())

        // when
        repository.addToApplicationContext(clazz, clazzInstance)

        // then
        assertThat(getApplicationContextComponentsSize()).isEqualTo(1)
    }

    @Test
    fun `Get class instance by canonical name`() {
        // given
        val clazz = Clazz(Foo::class.java)
        val clazzInstance = Instance(clazz.value.getDeclaredConstructor().newInstance())
        repository.addToApplicationContext(clazz, clazzInstance)
        assertThat(getApplicationContextComponentsSize()).isEqualTo(1)

        // when
        val clazzInstanceByCanonicalName = repository.getClazzInstanceByCanonicalName(clazz.getCanonicalName())

        // then
        assertThat(clazzInstanceByCanonicalName).isEqualTo(clazzInstance)
    }

    @Test
    fun `Get empty instance when canonical name does not exist in application context`() {
        // given
        val clazz = Clazz(Foo::class.java)
        val clazzInstance = Instance(clazz.value.getDeclaredConstructor().newInstance())
        repository.addToApplicationContext(clazz, clazzInstance)
        assertThat(getApplicationContextComponentsSize()).isEqualTo(1)

        // when
        val clazzInstanceByCanonicalName =
            repository.getClazzInstanceByCanonicalName(ClassCanonicalName("non-existing clazz"))

        // then
        assertThat(clazzInstanceByCanonicalName).isNull()
    }

    @Test
    fun `Get instance from container`() {
        val instance = Instance(Foo())
        val clazz = Clazz(Foo::class.java)
        repository.fillDIContainer(instance, clazz)

        // when
        val actualInstance = repository.getFirstClazzInstanceFromContainerByCanonicalName(clazz.getCanonicalName())

        // then
        assertThat(actualInstance).isEqualTo(instance)
    }

    private fun getApplicationContextComponentsSize() =
        InMemoryApplicationContextRepository.Context.applicationContext.getComponents().values.size

    private fun clearContext() {
        InMemoryApplicationContextRepository.Context.applicationContext = ApplicationContext(Components(emptyList()))
        InMemoryApplicationContextRepository.Context.container = Container()
    }
}
