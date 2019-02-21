package es.lolrav.podsavior.data

interface OverlayInsertItem<T> {
    fun insert(into: List<T>, item: T): List<T>
}