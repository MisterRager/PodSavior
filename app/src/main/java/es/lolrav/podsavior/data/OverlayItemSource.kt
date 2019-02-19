package es.lolrav.podsavior.data

import io.reactivex.Flowable
import io.reactivex.Scheduler

class OverlayItemSource<T>(
        private val scheduler: Scheduler,
        private val itemSources: Iterable<ItemSource<T>>,
        private val overlayFn: (List<T>, T) -> List<T>
) : ItemSource<T> {
    override fun findByName(name: CharSequence): Flowable<List<T>> =
            Flowable.fromIterable(itemSources)
                    .flatMap { source ->
                        source.findByName(name).subscribeOn(scheduler)
                    }
                    .scan(listOf<T>()) { results, newResults -> newResults.fold(results, overlayFn) }
                    // Skip the empty one that scan introduces
                    .skip(1)

    companion object {
        inline fun <T> buildMatchAndMergeOverlayFn(
                crossinline matchFn: (T, T) -> Boolean,
                crossinline mergeFn: (T, T) -> T
        ): (List<T>, T) -> List<T> = { results, item ->
            val copy = results.toList()
            copy.indexOfFirst { matchFn(item, it) }
                    .let { matchIndex -> if (matchIndex >= 0) matchIndex else null }
                    ?.let { matchIndex ->
                        copy.subList(0, matchIndex) +
                                mergeFn(copy[matchIndex], item) +
                                if (matchIndex < copy.size) {
                                    copy.subList(matchIndex + 1, copy.size)
                                } else {
                                    emptyList()
                                }
                    } ?: (copy + item)
        }
    }
}