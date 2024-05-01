package org.sdi.domain.model

import com.example.useraccount.services.UserAccountClient
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.sdi.helper.Foo

/**
 * @author Luis Miguel Barcos
 */
class ContainerTest {
    private lateinit var container: Container

    @BeforeEach
    fun setUp() {
        container = Container()
    }

    @Test
    fun `Add implementation to an empty container`() {
        // given
        val canonicalName = ClassCanonicalName(Foo::class.java.canonicalName)
        val instance = Instance(Foo())
        val implementation = Implementation(instance)

        // when
        container.addImplementation(canonicalName, implementation)

        // then
        assertThat(container.getImplementations(canonicalName)).isEqualTo(Implementations(listOf(implementation)))
    }

    @Test
    fun `Add Implementation to a non-empty container`() {
        // given
        val canonicalName = ClassCanonicalName(Foo::class.java.canonicalName)
        val instance = Instance(Foo())
        val implementation = Implementation(instance)
        anImplementations()

        // when
        container.addImplementation(canonicalName, implementation)

        // then
        assertThat(container.getImplementations(canonicalName)).isEqualTo(Implementations(listOf(implementation)))
    }

    @Test
    fun `Get implementations from the container`() {
        // given
        val fooImplementation = Implementation(Instance(Foo()))
        val userClientImplementation = Implementation(Instance(UserAccountClient()))
        anImplementations(fooImplementation, userClientImplementation)

        // when
        val actualImplementations = container.getImplementations(
            ClassCanonicalName(UserAccountClient::class.java.canonicalName)
        ) ?: Implementations(emptyList())

        // then
        assertThat(actualImplementations.values)
            .hasSize(2)
            .contains(
                fooImplementation,
                userClientImplementation
            )
    }

    @Test
    fun `Get no implementation when given class has no implementation in the container`() {
        // then
        assertThat(container.getImplementations(ClassCanonicalName("non-existing class"))).isNull()
    }

    @Test
    fun `Return container values when is empty`() {
        // then
        assertThat(container.values()).isEmpty()
    }

    @Test
    fun `Return container values when has values`() {
        // given
        anImplementations()

        // when-then
        assertThat(container.values())
            .hasSize(1)
    }

    private fun anImplementations(
        foo: Implementation = Implementation(Instance(Foo())),
        userAccountClient: Implementation = Implementation(Instance(UserAccountClient())),
    ) {
        container.addImplementation(
            ClassCanonicalName(UserAccountClient::class.java.canonicalName),
            foo
        )
        container.addImplementation(
            ClassCanonicalName(UserAccountClient::class.java.canonicalName),
            userAccountClient
        )
    }
}