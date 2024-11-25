package modules

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.core.context.loadKoinModules
import org.koin.core.logger.Level
import org.koin.core.logger.PrintLogger
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.junit5.KoinTestExtension
import kotlin.test.Test

private class RepositoryFoo(val dataSource: DataSource)
interface DataSource
private class LocalDataSource : DataSource
private class RemoteDatasource : DataSource

class LinkingModules : KoinTest {

    val repositoryModule = module {
        single { RepositoryFoo(get()) }
    }

    val localDataSourceModule = module {
        single<DataSource> { LocalDataSource() }
    }

    val remoteDataSourceModule = module {
        single<DataSource> { RemoteDatasource() }
    }

    @JvmField
    @RegisterExtension
    val koinExtensionOne = KoinTestExtension.create {
        logger(PrintLogger(Level.DEBUG))
        modules(repositoryModule, localDataSourceModule)
    }


    @Test
    fun test(){

    }

}

