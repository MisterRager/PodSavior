package es.lolrav.podsavior.net.itunes

import es.lolrav.podsavior.net.itunes.entity.ITunesSearchResults
import es.lolrav.podsavior.net.itunes.entity.ITunesSeries
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Query

const val ITUNES_BASE_URI = "https://itunes.apple.com"

interface ITunesService {
    @GET("/search")
    fun search(
            @Query("term") searchQuery: String,
            @Query("media") mediaType: String = "podcast",
            @Query("type") type: String = "podcast"
    ): Flowable<ITunesSearchResults>

    @GET("/lookup")
    fun details(
            @Query("id") id: String
    ): Flowable<ITunesSeries>
}
