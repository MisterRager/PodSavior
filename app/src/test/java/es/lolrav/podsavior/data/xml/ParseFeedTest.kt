package es.lolrav.podsavior.data.xml

import es.lolrav.podsavior.data.time.PatternMatchingParser
import es.lolrav.podsavior.database.entity.Series
import es.lolrav.podsavior.di.DaggerFeedParserComponent
import es.lolrav.podsavior.di.SeriesRandom
import org.junit.Assert.*
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

        val series = results.first().first
        assertNotNull(series)
        assertEquals("Pod Save America", series!!.name)
        assertEquals(
                "https://content.production.cdn.art19.com/images/9f/11/57/4c/9f11574c-4033-45aa-b8d3-0b204258ab58/5e963f8f8811e208b1a90c91507a0f7880c7b861bf81c290e44c193e891a507bbe99798dd7ed0cd795a4fede54560f167fa023553f66c29ff86bf9f8322c7f83.jpeg",
                series.iconPath)

        val episodes = results.drop(1).map { it.second!! }

        assertTrue(episodes.isNotEmpty())
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