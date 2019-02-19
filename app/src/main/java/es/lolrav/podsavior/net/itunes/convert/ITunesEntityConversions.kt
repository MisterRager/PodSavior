package es.lolrav.podsavior.net.itunes.convert

import dagger.Reusable
import es.lolrav.podsavior.database.entity.Series
import es.lolrav.podsavior.net.itunes.entity.ITunesSeries
import javax.inject.Inject

@Reusable
class ITunesEntityConversions
@Inject
constructor() {
    fun resultToSeries(result: ITunesSeries): Series =
            Series(
                    uid = "iTunes:${result.id}",
                    name = result.name,
                    feedUri = result.rssFeedUrl ?: "",
                    iconPath = result.feedIconUrl,
                    artistName = result.artistName)
}