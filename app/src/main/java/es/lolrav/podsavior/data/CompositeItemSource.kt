package es.lolrav.podsavior.data

import io.reactivex.Flowable

class CompositeItemSource<T>(
        private vararg val sources: ItemSource<T>
) : ItemSource<T> {
    override fun findByName(name: String): Flowable<List<T>> =
            sources.map { it.findByName(name).startWith(listOf<T>()) }
                    .let {
                        Flowable.combineLatest<List<T>, List<T>>(it) { lists ->
                            (lists as Array<List<T>>).asList().flatten()
                        }
                    }
}