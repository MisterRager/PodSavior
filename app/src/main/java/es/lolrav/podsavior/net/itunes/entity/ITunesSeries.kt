package es.lolrav.podsavior.net.itunes.entity

import com.squareup.moshi.Json

data class ITunesSeries
@JvmOverloads
constructor(
        @Json(name = "trackId")
        val id: String,
        @Json(name = "trackName")
        val name: String,
        @Json(name = "artistId")
        val artistId: Long,
        @Json(name = "artistName")
        val artistName: String,
        @Json(name = "feedUrl")
        val rssFeedUrl: String,

        @Transient
        val isSubscribed: Boolean = false
)