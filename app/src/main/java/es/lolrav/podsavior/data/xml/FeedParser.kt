package es.lolrav.podsavior.data.xml

import android.util.Log
import es.lolrav.podsavior.database.entity.Episode
import es.lolrav.podsavior.database.entity.Series
import org.threeten.bp.Duration
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream
import java.nio.charset.Charset
import java.util.concurrent.atomic.AtomicBoolean

typealias Or<A, B> = Pair<A?, B?>

class FeedParser(private val parser: XmlPullParser, private val series: Series) {
    fun parse(inputStream: InputStream): Sequence<Or<Series, Episode>> =
            sequence {
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true)
                parser.setInput(inputStream.bufferedReader(Charset.forName("UTF-8")))

                // Start reading
                parser.nextTag()
                //parser.require(XmlPullParser.START_TAG, null, "rss")

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
                            "itunes" to "image" -> iconPath = parser.nextText()
                            "atom10" to "link" -> {
                                if ("self" == parser.getAttributeValue(null, "rel")) {
                                    feedUri =
                                            parser.getAttributeValue(null, "href")
                                }
                            }
                            null to "item" -> {
                                // Dump the series data into the stream
                                if (hasStartedItems.compareAndSet(false, true)) {
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

        while (eventType != XmlPullParser.END_TAG || currentName != "item") {
            if (eventType == XmlPullParser.START_TAG) {

                when (parser.prefix to parser.name) {
                    null to "title" -> name = parser.nextText()
                    "itunes" to "title" -> name = name ?: parser.nextText()
                    "itunes" to "summary" -> description = parser.nextText()
                    "content" to "encoded" -> descriptionMarkup = parser.nextText()
                    "itunes" to "image" -> imageUri = parser.nextText()
                    "itunes" to "duration" -> {
                        val durationParts = parser.nextText().split(":").asReversed()

                        val seconds: Long = durationParts[0].toLong()
                        val minutes: Long = if (durationParts.size > 1) durationParts[1].toLong() else 0
                        val hours: Long = if (durationParts.size > 2) durationParts[2].toLong() else 0

                        duration = Duration.ofHours(hours)
                                .plusMinutes(minutes)
                                .plusSeconds(seconds)
                    }
                    null to "enclosure" -> audioUri = parser.getAttributeValue(null, "url")
                }
            }

            eventType = parser.next()
            currentName = parser.name
        }

        if (name != null && audioUri != null && duration != null) {
            yield(
                    null to Episode(
                            uid = audioUri,
                            name = name,
                            episodeUri = audioUri,
                            description = description,
                            descriptionMarkup = descriptionMarkup,
                            imageUri = imageUri,
                            duration = duration,
                            seriesUid = series.uid))
        } else {
            //Log.w(javaClass.simpleName, "Did not find enough fields for item \"$name\"")
        }
    }
}

