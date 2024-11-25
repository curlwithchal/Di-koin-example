package definitions

import org.junit.jupiter.api.assertInstanceOf
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.core.component.getScopeName
import org.koin.core.logger.Level
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.junit5.KoinTestExtension
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

private class Session
private class FooService
private class BarService(val fooService: FooService)

private class A
private class B
private class C


class ScopeTest : KoinTest {

    val linkingModule = module {
        single { A() }
        scope<A> {
            scoped { B() }
        }
        scope<B> {
            scoped { C() }
        }
    }

    val fooModule = module {
        single { FooService() }
        scope<FooService> {
            scoped { BarService(get()) }
        }
    }

    val appModule = module {
        scope<Session> {
            scoped {
                Session()
            }
        }
    }


    @JvmField
    @RegisterExtension
    val extensionStart = KoinTestExtension.create {
        printLogger(Level.DEBUG)
        modules(linkingModule)
    }

    @Test
    fun test() {
        // limit lifetime
        val create = getKoin().createScope<Session>()
        assertFalse(create.closed)
        // close instance
        create.close()
        assertTrue(create.closed)
    }

    @Test
    fun `test getting instance`() {
        val fooService = getKoin().get<FooService>()
        val scope = getKoin().createScope<FooService>()
        val barService = scope.get<BarService>()
        assertEquals(barService.fooService, fooService)
    }

    @Test
    fun `test linking module scope instance`(){
        val scopeA = getKoin().createScope<A>()
        val scopeB = getKoin().createScope<B>()
        // joined scope
        scopeA.linkTo(scopeB)

        // we got the same C instance from A or B scope
        assertEquals(scopeA.get<C>(), scopeB.get<C>())
    }
}