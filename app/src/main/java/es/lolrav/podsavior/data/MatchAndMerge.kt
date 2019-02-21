package es.lolrav.podsavior.data

interface MatchAndMerge<T> {
    fun matches(left: T, right: T): Boolean
    fun merge(bottom: T, top: T): T
}