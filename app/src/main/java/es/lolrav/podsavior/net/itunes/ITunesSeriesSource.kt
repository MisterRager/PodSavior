package es.lolrav.podsavior.net.itunes

import es.lolrav.podsavior.data.ItemSource
import es.lolrav.podsavior.database.entity.Series
import es.lolrav.podsavior.net.itunes.entity.ITunesSearchResults
import es.lolrav.podsavior.net.itunes.entity.ITunesSeries
import io.reactivex.Flowable

class ITunesSeriesSource(
        private val service: ITunesService,
        private val converter: (ITunesSeries) -> Series
) : ItemSource<Series> {
    override fun findByName(name: String): Flowable<List<Series>> =
            service.search(name)
                    .map(ITunesSearchResults::results)
                    .map { results -> results.map(converter) }
}