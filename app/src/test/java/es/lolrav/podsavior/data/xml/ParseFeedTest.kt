package es.lolrav.podsavior.data.xml

import es.lolrav.podsavior.data.time.PatternMatchingParser
import es.lolrav.podsavior.database.entity.Series
import es.lolrav.podsavior.di.DaggerFeedParserComponent
import es.lolrav.podsavior.di.SeriesRandom
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream
import javax.inject.Inject

class ParseFeedTest {
    @[Inject SeriesRandom]
    lateinit var series: Series
    @Inject
    lateinit var parser: XmlPullParser
    @Inject
    lateinit var timeParser: PatternMatchingParser

    private lateinit var parseOperator: ParseFeed

    @Before
    fun setup() {
        DaggerFeedParserComponent.builder().build().inject(this)
        parseOperator = ParseFeed(parser, series)
    }

    @Test
    fun testAmerica() {
        val results = parseOperator.parse(americaXml).toList()

        assertFalse(results.isEmpty())
    }

    @Test
    fun testLab() {
        val results = parseOperator.parse(labXml).toList()

        assertFalse(results.isEmpty())
    }

    private val americaXml: InputStream
        get() = javaClass.classLoader!!.getResourceAsStream("america_rss.xml")

    private val labXml: InputStream
        get() = javaClass.classLoader!!.getResourceAsStream("lab.xml")
}