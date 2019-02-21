package es.lolrav.podsavior.data

interface OverlayProcessor<T> {
    fun overlay(current: List<T>, next: List<T>): List<T>
}