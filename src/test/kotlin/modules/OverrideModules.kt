package modules

import org.koin.test.KoinTest

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertInstanceOf
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.logger.PrintLogger
import org.koin.dsl.module
import org.koin.test.inject
import kotlin.test.Test


private open class Repository

private class DataSourceLocal : Repository()
private class DataSourceRemote : Repository()

class OverrideModules: KoinTest {

    private val repository: Repository by inject()

    val localModule = module {
        single<Repository> { DataSourceLocal() }
    }

    val remoteModule = module {
        single<Repository> { DataSourceRemote() }
    }


    @BeforeEach
    fun `init`() {
        startKoin {
            // forbidden definition override
//             allowOverride(false)
            logger(PrintLogger(org.koin.core.logger.Level.DEBUG))
            // override -> DataSourceRemote
            modules(localModule, remoteModule)
        }
    }

    @AfterEach
    fun `stop koin`() {
        stopKoin()
    }

    @Test
    fun `test override modules`() {
        when(repository){
            is DataSourceLocal -> println("not match type")
            is DataSourceRemote -> println("match type")
        }
        //assertInstanceOf<DataSourceLocal>(repository) // Error
        assertInstanceOf<DataSourceRemote>(repository) // Correct
    }
}
