package definitions

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.parameter.parameterArrayOf
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertEquals

private class Fly
private class Cat
private class Dog
private class Animal(val cat: Cat, val dog: Dog)
private class Bird(val fly: Fly)


class InjectedParameter : KoinTest {

    val appModule = module {
        // Destructured One not type safe
        single { params ->
            Animal(cat = params.get(), dog = params.get())
        }

        // Destructured Two not type safe
        single { (fly: Fly) -> Bird(fly) }
    }

    val appModuleTwo = module {
        // type safe
        single { p -> Animal(p[0], p[1]) }
        single{ Fly() }
        single {
            Bird(get())
        }
    }

    @BeforeEach
    fun `init module`() {
        startKoin { modules(appModuleTwo) }
    }

    @AfterEach
    fun `stop koin`() {
        stopKoin()
    }

    @Test
    fun `test injected Parameters`() {
        val cat: Cat = Cat()
        val dog: Dog = Dog()
        val animal: Animal by inject() { parametersOf(cat,dog) }
        val bird: Bird by inject()
        val fly: Fly by inject()


        assertEquals(cat, animal.cat)
        assertEquals(dog, animal.dog)
        assertEquals(fly, bird.fly)
    }


}