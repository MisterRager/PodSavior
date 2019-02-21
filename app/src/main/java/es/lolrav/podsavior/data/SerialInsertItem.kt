package es.lolrav.podsavior.data

class SerialInsertItem<T>(private val matchAndMerge: MatchAndMerge<T>) : OverlayInsertItem<T> {
    override fun insert(into: List<T>, item: T): List<T> {
        val copy = into.toList()
        return copy
                .indexOfFirst { matchAndMerge.matches(item, it) }
                .let { matchIndex -> if (matchIndex >= 0) matchIndex else null }
                ?.let { matchIndex ->
                    // Head
                    copy.subList(0, matchIndex) +
                            // Updated item
                            matchAndMerge.merge(copy[matchIndex], item) +
                            // Tail
                            if (matchIndex < copy.size) {
                                copy.subList(matchIndex + 1, copy.size)
                            } else {
                                emptyList()
                            }
                } ?: (copy + item)
    }
}