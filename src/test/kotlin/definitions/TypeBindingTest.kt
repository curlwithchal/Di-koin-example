package definitions

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertInstanceOf
import org.koin.core.component.getScopeName
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.logger.Level
import org.koin.core.logger.PrintLogger
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

interface Service {
    fun doSomething()
}

class FooServiceImpl() : Service{
    override fun doSomething() {
        TODO("Not yet implemented")
    }
}

class BarServiceImpl() : Service {
    override fun doSomething() {
        TODO("Not yet implemented")
    }

}


class TypeBindingTest : KoinTest {

    private val fooServiceImpl: FooServiceImpl by inject()
    private val service: Service by inject()

    val appModuleBind = module {
//        // match type is FooServiceImpl only
//        single { FooServiceImpl() }
//
//        // match type Service only
//        single{ FooServiceImpl() as Service }
//
//        //2nd style type match Service not type cast as
//        single<Service>{fooServiceImpl}

        // match types Service & FooServiceImpl
        single { FooServiceImpl() } bind Service::class
    }

    val namingBindModule = module {
        single<Service>(named("fooOne")){ FooServiceImpl() }
        single<Service>(named("fooTwo")){ FooServiceImpl() }
    }

    @BeforeEach
    fun `init module`() {
        startKoin {
            logger(PrintLogger(Level.DEBUG))
            modules(namingBindModule)
        }
    }

    @AfterEach
    fun `close koin`() {
        stopKoin()
    }

    @Test
    fun `test type binding`() {

        when (service) {
            is FooServiceImpl -> println("Type Match")
            else -> println("Not Match Type")
        }

        assertInstanceOf<Service>(fooServiceImpl)
        assertInstanceOf<FooServiceImpl>(service)
    }

    @Test
    fun `test naming & default binding`() {
        val serviceOne: Service by inject(named("fooOne"))
        val serviceTwo: Service by inject(named("fooTwo"))

        assertNotEquals(serviceOne, serviceTwo)
    }
}