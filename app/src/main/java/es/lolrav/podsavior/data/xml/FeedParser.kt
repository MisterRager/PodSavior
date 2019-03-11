package es.lolrav.podsavior.data.xml

import es.lolrav.podsavior.database.entity.Episode
import es.lolrav.podsavior.database.entity.Series
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream

class FeedParser(private val parser: XmlPullParser, private val series: Series) {
    fun parse(inputStream: InputStream): Sequence<Episode> =
            sequence {
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true)
            }
}