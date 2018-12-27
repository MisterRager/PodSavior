package es.lolrav.podsavior.net.itunes.entity

import com.squareup.moshi.Moshi
import es.lolrav.podsavior.di.NetModule
import okio.Okio
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ITunesSearchResultsTest {
    private val moshi: Moshi get() = NetModule().providesMoshi()

    @Test
    fun testParseJson() {
        val parsed = moshi
                .adapter(ITunesSearchResults::class.java)
                .fromJson(Okio.buffer(Okio.source(
                        javaClass.classLoader!!
                                .getResourceAsStream("america_itunes.json")
                )))

        assertNotNull(parsed)
        assertEquals(50, parsed!!.count)

        parsed.results[0].let { series ->
            assertEquals(1192761536, series.id)
            assertEquals("Pod Save America", series.name)
            assertEquals(1200975667, series.artistId)
            assertEquals("Crooked Media", series.artistName)
            assertEquals("http://feeds.feedburner.com/pod-save-america", series.rssFeedUrl)
        }
    }
}