package es.lolrav.podsavior.data.rx

import com.google.common.util.concurrent.ListenableFuture
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.ReplayProcessor
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

class RxListenableFuture<T>(
        private val single: Single<T>
) : ListenableFuture<T> {
    private val bus: ReplayProcessor<T> = ReplayProcessor.createWithSize(1)
    private val disposable: CompositeDisposable by lazy { CompositeDisposable() }
    private val isInitialized: AtomicBoolean by lazy { AtomicBoolean(false) }
    private val isDone: AtomicBoolean by lazy { AtomicBoolean(false) }
    private val isCancelled: AtomicBoolean by lazy { AtomicBoolean(false) }

    override fun addListener(listener: Runnable, executor: Executor) {
        synchronized(isInitialized) {
            if (isInitialized.compareAndSet(false, true)) {
                single
                        .doOnError { isDone.set(true) }
                        .doOnSuccess { isDone.set(true) }
                        .subscribe(bus::onNext, bus::onError)
                        .let(disposable::add)
            }
        }

        bus.firstOrError()
                .observeOn(Schedulers.from(executor))
                .subscribe { _ -> listener.run() }
                .let(disposable::add)
    }

    override fun isDone(): Boolean = isDone.get()

    override fun get(): T = bus.firstOrError().blockingGet()

    override fun get(timeout: Long, unit: TimeUnit?): T =
            bus.firstOrError().timeout(timeout, unit).blockingGet()

    override fun cancel(mayInterruptIfRunning: Boolean): Boolean {
        synchronized(isInitialized) {
            disposable.clear()
            isCancelled.set(true)
        }
        return true
    }

    override fun isCancelled(): Boolean = isCancelled.get()
}