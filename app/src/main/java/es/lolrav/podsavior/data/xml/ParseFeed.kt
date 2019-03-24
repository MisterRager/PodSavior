package es.lolrav.podsavior.data.xml

import android.util.Log
import es.lolrav.podsavior.database.entity.Episode
import es.lolrav.podsavior.database.entity.Series
import org.threeten.bp.*
import org.threeten.bp.format.DateTimeFormatter
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream
import java.nio.charset.Charset
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

typealias Or<A, B> = Pair<A?, B?>

private val TAG = ParseFeed::class.java.simpleName

class ParseFeed(
        private val parser: XmlPullParser,
        private val series: Series
) {
    fun parse(inputStream: InputStream): Sequence<Or<Series, Episode>> =
            sequence {
                Log.v(TAG, "Start parsing Feed for ${series.name}")

                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true)
                parser.setInput(inputStream.bufferedReader(Charset.forName("UTF-8")))

                // Start reading
                parser.nextTag()
                parser.require(XmlPullParser.START_TAG, null, "rss")

                var eventType = parser.eventType

                var title: String? = null
                var description: String? = null
                var artistName: String? = null
                var iconPath: String? = null
                var feedUri: String? = null

                val hasStartedItems = AtomicBoolean(false)

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        when (parser.prefix to parser.name) {
                            null to "title" -> title = parser.nextText()
                            null to "description" -> description = parser.nextText()
                            "itunes" to "author" -> artistName = parser.nextText()
                            "itunes" to "summary" -> description = description ?: parser.nextText()
                            "itunes" to "image" -> iconPath = parser.getAttributeValue(null, "href")
                            null to "image" -> {
                                var tagType = parser.nextTag()

                                do {
                                    if (tagType == XmlPullParser.START_TAG) {
                                        when (parser.name) {
                                            "url" -> {
                                                iconPath = if (iconPath.isNullOrBlank()) {
                                                    parser.nextText().trim()
                                                } else {
                                                    iconPath
                                                }
                                            }
                                        }
                                    }
                                    tagType = parser.next()
                                } while (tagType != XmlPullParser.END_TAG || parser.name != "image")
                            }
                            "atom10" to "link" -> {
                                if ("self" == parser.getAttributeValue(null, "rel")) {
                                    feedUri =
                                            parser.getAttributeValue(null, "href")
                                                    .trim()
                                }
                            }
                            "atom" to "link" -> {
                                if ("self" == parser.getAttributeValue(null, "rel")) {
                                    feedUri =
                                            parser.getAttributeValue(null, "href")
                                                    .trim()
                                }
                            }
                            null to "item" -> {
                                // Dump the series data into the stream
                                if (hasStartedItems.compareAndSet(false, true)) {
                                    Log.v(TAG, "Yield Updated Series [$title]!")
                                    yield(
                                            Series(
                                                    uid = series.uid,
                                                    name = title ?: series.name,
                                                    description = description ?: series.description,
                                                    artistName = artistName ?: series.artistName,
                                                    iconPath = iconPath ?: series.iconPath,
                                                    feedUri = feedUri ?: series.feedUri,
                                                    isSaved = series.isSaved,
                                                    isSubscribed = series.isSubscribed
                                            ) to null)
                                }

                                parseItem(parser, series)
                            }
                        }
                    }

                    eventType = parser.next()
                }
            }

    private suspend fun SequenceScope<Or<Series, Episode>>.parseItem(
            parser: XmlPullParser,
            series: Series
    ) {
        var eventType = parser.eventType
        var currentName = parser.name

        var name: String? = null
        var description: String? = null
        var descriptionMarkup: String? = null
        var audioUri: String? = null
        var imageUri: String? = null
        var duration: Duration? = null
        var publishTime: Instant? = null

        val timeRegex =
                "[A-Z][a-z]+, ([0-9]+) ([A-Z][a-z]+) ([0-9]+) ([0-9]+):([0-9]+):([0-9]+) (.*)"
                        .toRegex()

        while (eventType != XmlPullParser.END_TAG || currentName != "item") {
            if (eventType == XmlPullParser.START_TAG) {

                when (parser.prefix to parser.name) {
                    null to "title" -> name = parser.nextText().trim()
                    "itunes" to "title" -> name = name ?: parser.nextText().trim()
                    "itunes" to "summary" -> description = parser.nextText().trim()
                    "content" to "encoded" -> descriptionMarkup = parser.nextText().trim()
                    "itunes" to "image" -> imageUri = parser.nextText().trim()
                    "itunes" to "duration" -> {
                        val durationParts = parser.nextText().split(":")

                        when (durationParts.size) {
                            0 -> duration = Duration.ofMillis(0)
                            1 -> duration = Duration.ofSeconds(durationParts[0].toLong())
                            2 -> duration = Duration.ofMinutes(durationParts[0].toLong()).plusSeconds(durationParts[1].toLong())
                            else -> duration = Duration.ofHours(durationParts[0].toLong()).plusMinutes(durationParts[1].toLong()).plusSeconds(durationParts[2].toLong())
                        }
                    }
                    null to "pubDate" -> {
                        // Mon, 11 Jun 2018 13:03:00 PDT
                        val timeStr = parser.nextText().trim()

                        publishTime = if (timeStr.matches(".* [A-Z]+$".toRegex())) {
                            timeRegex.matchEntire(timeStr).let { timeMatches ->
                                LocalDate
                                        .of(
                                                timeMatches!!.groupValues[3].toInt(),
                                                monthNumber(timeMatches.groupValues[2]),
                                                timeMatches.groupValues[1].toInt())
                                        .atTime(
                                                timeMatches.groupValues[4].toInt(),
                                                timeMatches.groupValues[5].toInt(),
                                                timeMatches.groupValues[6].toInt())
                                        .atZone(DateTimeUtils.toZoneId(TimeZone.getTimeZone(timeMatches.groupValues[7])))
                                        .toInstant()
                            }
                        } else {
                            DateTimeFormatter.RFC_1123_DATE_TIME.parse(timeStr, OffsetDateTime.FROM).toInstant()
                        }
                    }
                    null to "enclosure" -> audioUri = parser.getAttributeValue(null, "url").trim()
                }
            }

            eventType = parser.next()
            currentName = parser.name
        }

        if (name != null && audioUri != null && duration != null && publishTime != null) {
            Log.v(TAG, "Yield Updated Episode [$name]!")
            yield(
                    null to Episode(
                            uid = audioUri,
                            name = name,
                            episodeUri = audioUri,
                            description = description,
                            descriptionMarkup = descriptionMarkup,
                            imageUri = imageUri,
                            duration = duration,
                            seriesUid = series.uid,
                            publishTime = publishTime,
                            onDiskPath = null))
        } else {
            Log.w(TAG, "Did not find enough fields for item \"$name\"")
        }
    }

    private fun monthNumber(name: String): Int =
            when (name) {
                "Jan" -> 1
                "Feb" -> 2
                "Mar" -> 3
                "Apr" -> 4
                "May" -> 5
                "Jun" -> 6
                "Jul" -> 7
                "Aug" -> 8
                "Sep" -> 9
                "Oct" -> 10
                "Nov" -> 11
                "Dec" -> 12
                else -> -1
            }
}

