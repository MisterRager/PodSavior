package es.lolrav.podsavior.data.xml

import es.lolrav.podsavior.di.TestSeriesModule.providesSeries
import es.lolrav.podsavior.di.TestSeriesModule.providesSeriesRandom
import es.lolrav.podsavior.di.module.XmlParserModule.providesXmlParser
import es.lolrav.podsavior.di.module.XmlParserModule.providesXmlParserFactory
import org.junit.Assert.assertFalse
import org.junit.Test
import java.io.InputStream

class FeedParserTest {
    @Test
    fun testAmerica() {
        val series = providesSeries(providesSeriesRandom())
        val parser = FeedParser(providesXmlParser(providesXmlParserFactory()), series)

        val results = parser.parse(americaXml).toList()

        assertFalse(results.isEmpty())
    }

    private val americaXml: InputStream
        get() = javaClass.classLoader!!.getResourceAsStream("america_rss.xml")
}