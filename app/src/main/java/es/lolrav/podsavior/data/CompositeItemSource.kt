package es.lolrav.podsavior.data

import io.reactivex.Flowable

class CompositeItemSource<T>(
        private vararg val sources: ItemSource<T>,
        private val reducer: (List<T>, List<T>) -> List<T> = { a, b -> a + b }
) : ItemSource<T> {
    override fun findByName(
            name: CharSequence
    ): ItemSourceResultStream<T> =
            Flowable.fromArray(*sources)
                    .flatMap { it.findByName(name) }
                    .scan(reducer)
}