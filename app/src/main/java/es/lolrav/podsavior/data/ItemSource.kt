package es.lolrav.podsavior.data

import io.reactivex.Flowable

typealias ItemSourceStreamFactory<T> = (CharSequence) -> Flowable<List<T>>

interface ItemSource<T> {
    fun findByName(name: CharSequence): Flowable<List<T>>

    private class ItemSourceImpl<T>(
            private val finder: ItemSourceStreamFactory<T>
    ) : ItemSource<T> {
        override fun findByName(name: CharSequence): Flowable<List<T>> = finder(name)
    }

    companion object {
        fun <T> by(finder: ItemSourceStreamFactory<T>): ItemSource<T> = ItemSourceImpl(finder)
    }
}