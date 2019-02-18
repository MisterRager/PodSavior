package es.lolrav.podsavior.data

import io.reactivex.Flowable

interface ItemSource<T> {
    fun findByName(name: String): Flowable<List<T>>

    private class ItemSourceImpl<T>(
            private val finder: (String) -> Flowable<List<T>>
    ) : ItemSource<T> {
        override fun findByName(name: String): Flowable<List<T>> = finder(name)
    }

    companion object {
        fun <T> by(finder: (String) -> Flowable<List<T>>): ItemSource<T> = ItemSourceImpl(finder)
    }
}