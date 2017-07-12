package pl.ciurkot

import kotlinx.coroutines.experimental.Unconfined
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.channels.produce

//[prev] always remains `null` even after many iterations
fun <T> ReceiveChannel<T>.reduce(reducer: (T, T) -> T) = produce<T>(Unconfined) {
    var prev: T? = null
    for (elem in this@reduce) {
        if (prev == null) {
            prev = elem
            send(elem)
        } else {
            val new = reducer(prev, elem)
            prev = new
            send(new)
        }
    }
}


private class Wrap<T>(var obj: T? = null)

//having immutable reference to mutable class `Wrap` fixes the issue
fun <T> ReceiveChannel<T>.reduceWrap(reducer: (T, T) -> T) = produce<T>(Unconfined) {
    val prev = Wrap<T>()
    for (elem in this@reduceWrap) {
        if (prev.obj == null) {
            prev.obj = elem
            send(elem)
        } else {
            val new = reducer(prev.obj!!, elem)
            prev.obj = new
            send(new)
        }
    }
}