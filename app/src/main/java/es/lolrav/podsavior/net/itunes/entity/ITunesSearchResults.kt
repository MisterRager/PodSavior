package es.lolrav.podsavior.net.itunes.entity

import com.squareup.moshi.Json

class ITunesSearchResults(
        @Json(name = "resultCount")
        val count: Int,
        @Json(name = "results")
        val results: List<ITunesSeries>)