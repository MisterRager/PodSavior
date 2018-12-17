package es.lolrav.podsavior.data

import io.reactivex.Flowable

interface ItemSource<T> {
    fun findByName(name: String): Flowable<List<T>>

    companion object {
        fun <T> by(finder: (String) -> Flowable<List<T>>): ItemSource<T> =
                object : ItemSource<T> {
                    override fun findByName(name: String): Flowable<List<T>> = finder(name)
                }
    }
}