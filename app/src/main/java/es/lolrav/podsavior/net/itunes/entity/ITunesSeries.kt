package es.lolrav.podsavior.net.itunes.entity

import com.squareup.moshi.Json

data class ITunesSeries(
        @Json(name = "trackId")
        val id: Int,
        @Json(name = "trackName")
        val name: String,
        @Json(name = "artistId")
        val artistId: Int?,
        @Json(name = "artistName")
        val artistName: String,
        @Json(name = "feedUrl")
        val rssFeedUrl: String?,
        @Json(name = "artworkUrl600")
        val feedIconUrl: String?)