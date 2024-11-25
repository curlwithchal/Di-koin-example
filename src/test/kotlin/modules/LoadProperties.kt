package modules

import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.logger.Level
import org.koin.dsl.module
import org.koin.fileProperties
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.junit5.KoinTestExtension
import kotlin.test.Test
import kotlin.test.assertEquals

private class HelloFoo(val property: String)

class LoadProperties : KoinTest {

    private val helloFoo: HelloFoo by inject()

    val propModule = module {
        single{
            HelloFoo(getProperty("foo"))
        }
    }

    @JvmField
    @RegisterExtension
    val extensionModule = KoinTestExtension.create {
        printLogger(Level.DEBUG)
        // change default koin.properties
        //fileProperties("/foo")
        fileProperties()
        modules(propModule)
    }

    @OptIn(KoinInternalApi::class)
    @Test
    fun `load properties`() {
        assertEquals("Hello Foo", helloFoo.property)
        assertEquals("Hello Bar", getKoin().getProperty("bar"))
    }
}