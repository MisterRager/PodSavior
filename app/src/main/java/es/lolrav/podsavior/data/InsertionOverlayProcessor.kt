package es.lolrav.podsavior.data

class InsertionOverlayProcessor<T>(
        private val insertion: OverlayInsertItem<T>
) : OverlayProcessor<T> {
    override fun overlay(
            current: List<T>,
            next: List<T>
    ): List<T> = next.fold(current, insertion::insert)
}