package modules

import org.junit.jupiter.api.BeforeEach
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.unloadKoinModules
import org.koin.core.logger.Level
import org.koin.dsl.module
import org.koin.test.KoinTest
import kotlin.test.Test


class Box
class Rectangle

class LoadModule: KoinTest{

    val appModule = module {
        single{ Box() }
    }

    val newModule = module{
        single{ Rectangle() }
    }

    @BeforeEach
    fun before(){
        startKoin{
            printLogger(Level.DEBUG)
            modules(appModule)
        }
    }

    @Test
    fun test(){
        // add module instance current run starKoin
        loadKoinModules(newModule)
        // unload bunch the release instance
        unloadKoinModules(newModule)
    }

}