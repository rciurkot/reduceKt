package pl.ciurkot

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.channels.produce
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.runBlocking

private fun numbers() = produce(CommonPool) {
    var i = 0
    while (true) {
        send(i++)
        delay(500)
    }
}

fun main(args: Array<String>) {

    val d = async(CommonPool) {
        //never reduces the data
        numbers().reduce { prev, curr -> prev + curr }.consumeEach { println(it) }
    }

    val dWrap = async(CommonPool) {
        //produces desired results
        numbers().reduceWrap { prev, curr -> prev + curr }.consumeEach { println("Wrap $it") }
    }

    runBlocking {
        d.await()
        dWrap.await()
    }
}
