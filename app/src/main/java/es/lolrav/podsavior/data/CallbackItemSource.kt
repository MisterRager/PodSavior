package es.lolrav.podsavior.data

import io.reactivex.Flowable

class CallbackItemSource<T>(
        private val callback: (CharSequence) -> Flowable<List<T>>
) : ItemSource<T> {
    override fun findByName(name: CharSequence): Flowable<List<T>> = callback(name)
}