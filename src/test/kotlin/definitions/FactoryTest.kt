package definitions

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull

private class ComponentFactory


class FactoryTest : KoinTest {

    private val factoryA: ComponentFactory by inject()
    private val factoryB: ComponentFactory by inject()

    val moduleFactory = module {
        factory {
            ComponentFactory()
        }
    }

    @BeforeEach
    fun `init Module`() {
        startKoin {
            modules(moduleFactory)
        }
    }

    @AfterEach
    fun `stop koin`() {
        stopKoin()
    }

    @Test
    fun `test expected factory instance`() {
        assertNotEquals(factoryA, factoryB)
        assertNotEquals(factoryB, factoryA)
        assertNotNull(factoryA, "Not null instance Factory A")
        assertNotNull(factoryB, "Not null instance Factory B")
    }
}