package es.lolrav.podsavior.data

import io.reactivex.Flowable
import io.reactivex.Scheduler

class OverlayItemSource<T>(
        private val scheduler: Scheduler,
        private val itemSources: Iterable<ItemSource<T>>,
        private val overlayProcessor: OverlayProcessor<T>
) : ItemSource<T> {
    override fun findByName(name: CharSequence): Flowable<List<T>> =
            Flowable.fromIterable(itemSources)
                    .flatMap { source -> source.findByName(name).subscribeOn(scheduler) }
                    .scan(overlayProcessor::overlay)
}

