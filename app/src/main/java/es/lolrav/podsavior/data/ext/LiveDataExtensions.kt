package es.lolrav.podsavior.data.ext

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import org.reactivestreams.Publisher
import androidx.lifecycle.LiveDataReactiveStreams

fun <T, R> LiveData<T>.switchMap(fn: (T) -> LiveData<R>) =
        Transformations.switchMap(this, fn)

fun <T, R> LiveData<T>.map(fn: (T) -> R) =
        Transformations.map(this, fn)

fun <T> Publisher<T>.toLiveData() = LiveDataReactiveStreams.fromPublisher(this)