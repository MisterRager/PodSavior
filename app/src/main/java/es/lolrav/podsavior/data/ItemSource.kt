package es.lolrav.podsavior.data

import io.reactivex.Flowable

typealias ItemSourceResultStream<T> = Flowable<List<T>>
typealias ItemSourceStreamFactory<T> = (CharSequence) -> ItemSourceResultStream<T>

interface ItemSource<T> {
    fun findByName(name: CharSequence): ItemSourceResultStream<T>

    private class ItemSourceImpl<T>(
            private val finder: ItemSourceStreamFactory<T>
    ) : ItemSource<T> {
        override fun findByName(name: CharSequence): ItemSourceResultStream<T> = finder(name)
    }

    companion object {
        fun <T> by(finder: ItemSourceStreamFactory<T>): ItemSource<T> = ItemSourceImpl(finder)
    }
}