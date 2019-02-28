package es.lolrav.podsavior.data

import javax.inject.Inject

class SerialInsertItem<T>
@Inject
constructor(private val matchAndMerge: MatchAndMerge<T>) : OverlayInsertItem<T> {
    override fun insert(into: List<T>, item: T): List<T> {
        val copy = into.toList()
        return copy
                .indexOfFirst { matchAndMerge.matches(item, it) }
                .let { matchIndex -> if (matchIndex >= 0) matchIndex else null }
                ?.let { matchIndex -> updateItemAt(copy, item, matchIndex) }
                ?: (copy + item)
    }

    private fun updateItemAt(into: List<T>, item: T, index: Int): List<T> =
            into.subList(0, index) +
                    matchAndMerge.merge(into[index], item) +
                    if (index < into.size) {
                        into.subList(index + 1, into.size)
                    } else {
                        emptyList()
                    }

}