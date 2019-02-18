package es.lolrav.podsavior.android

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable

fun <T> Flowable<T>.toLiveData(): LiveData<T> = LiveDataReactiveStreams.fromPublisher(this)
fun <T> Observable<T>.toLiveData(
        backPressureStrategy: BackpressureStrategy = BackpressureStrategy.LATEST
): LiveData<T> = toFlowable(backPressureStrategy).toLiveData()
