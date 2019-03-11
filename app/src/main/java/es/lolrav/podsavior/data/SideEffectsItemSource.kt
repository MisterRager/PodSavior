package es.lolrav.podsavior.data

import io.reactivex.Completable
import io.reactivex.Flowable

class SideEffectsItemSource<T>(
        private val base: ItemSource<T>,
        private val onEach: (List<T>) -> Completable
) : ItemSource<T> {
    override fun findByName(name: CharSequence): Flowable<List<T>> =
            base.findByName(name).flatMapSingle { list -> onEach(list).toSingleDefault(list) }
}