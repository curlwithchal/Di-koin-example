package modules

import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.verify.verify
import kotlin.test.Test

class ComponentModuleFoo
class ComponentModuleBar

class CheckModule: KoinTest {

    val fooModule = module {
        single<ComponentModuleFoo> {
            ComponentModuleFoo()
        }
    }

    val barModule = module {
        single<ComponentModuleBar> {
            ComponentModuleBar()
        }
    }

    val appModule = module {
        includes(fooModule, barModule)
    }

    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun testCheckModule(){
        appModule.verify()
    }

}