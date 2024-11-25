package definitions

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


private class Component

class SingletonTest : KoinTest {

    private val componentA: Component by inject()
    private val componentB: Component by inject()

    val componentModule = module {
        single { Component() }
    }

    @BeforeEach
    fun `initial Module`() {
        startKoin {
            modules(componentModule)
        }
    }

    @AfterEach
    fun closeKoin() {
        stopKoin()
    }

    @Test
    fun `expected singleton instance`() {
        assertEquals(componentA, componentB)
        assertEquals(componentB, componentA)
        assertNotNull(componentA, "Not null Instance ComponentA")
        assertNotNull(componentB, "Not null Instance ComponentB")
    }

}

private class Foo()
private class Bar(val foo: Foo)

class SingletonTestTwo : KoinTest {

    private val foo: Foo by inject()
    private val bar: Bar by inject()

    val appModule = module {
        single<Foo> {
            Foo()
        }
        single<Bar> {
            Bar(get()) // inject instance dependency Foo
        }
    }

    @BeforeEach
    fun `init module`(){
        startKoin {
            modules(appModule)
        }
    }

    @AfterEach
    fun `stop koin`(){
        stopKoin()
    }

    @Test
    fun `test inject dependency instance`() {
        assertEquals(foo, bar.foo)
        assertEquals(bar.foo, foo)
    }
}

