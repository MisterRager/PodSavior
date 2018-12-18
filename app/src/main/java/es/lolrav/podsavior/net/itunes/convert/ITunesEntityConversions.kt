package es.lolrav.podsavior.net.itunes.convert

import es.lolrav.podsavior.database.entity.Series
import es.lolrav.podsavior.net.itunes.entity.ITunesSeries
import javax.inject.Inject

class ITunesEntityConversions
@Inject
constructor() {
    fun resultToSeries(result: ITunesSeries): Series =
            Series(
                    uid = "iTunes:${result.id}",
                    name = result.name,
                    feedUri = result.rssFeedUrl)
}