package es.lolrav.podsavior.data.merge

import com.squareup.moshi.Moshi
import es.lolrav.podsavior.database.entity.Series
import es.lolrav.podsavior.di.module.NetModule
import es.lolrav.podsavior.net.itunes.entity.ITunesSearchResults
import es.lolrav.podsavior.di.DaggerMergeSeriesTestComponent
import okio.Okio
import org.junit.Assert.*
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import javax.inject.Inject

class MergeSeriesTest {
    @Inject
    lateinit var seriesA: Series
    @Inject
    lateinit var seriesB: Series
    @Inject
    lateinit var merger: MergeSeries

    @Before
    fun setup() {
        DaggerMergeSeriesTestComponent.builder().build().inject(this)
    }

    @Test
    fun testMatchOnUid() {
        assertTrue(merger.matches(seriesA, seriesA))
        assertTrue(merger.matches(seriesB, seriesB))
        assertTrue(merger.matches(seriesA, seriesA.copy(name = "updated")))

        assertFalse(merger.matches(seriesA, seriesB))
        assertFalse(merger.matches(seriesB, seriesA))
    }

    @Test
    fun `test that new data does not overwrite subscription status`() {
        val unsubscribedUnsaved = seriesA.copy(isSaved = false, isSubscribed = false)
        val savedAndSubscribed = seriesA.copy(isSaved = true, isSubscribed = true)

        val updateWithSaved = merger.merge(unsubscribedUnsaved, savedAndSubscribed)

        assertTrue(updateWithSaved.isSaved)
        assertTrue(updateWithSaved.isSubscribed)

        val updateAgainWithUnsaved = merger.merge(updateWithSaved, unsubscribedUnsaved)

        assertTrue(updateAgainWithUnsaved.isSaved)
        assertTrue(updateAgainWithUnsaved.isSubscribed)

        val unsubscribeWithSaved = merger.merge(
                updateAgainWithUnsaved,
                updateAgainWithUnsaved.copy(isSubscribed = false))

        assertTrue(unsubscribeWithSaved.isSaved)
        assertFalse(unsubscribeWithSaved.isSubscribed)
    }

    @Ignore
    @Test
    fun `full-on integration test`() {
        val moshi: Moshi = NetModule.providesMoshi()

        val parsed: ITunesSearchResults = moshi
                .adapter(ITunesSearchResults::class.java)
                .fromJson(Okio.buffer(Okio.source(
                        javaClass.classLoader!!
                                .getResourceAsStream("america_itunes.json"))))!!

        //merger.merge(parsed.results, parsed.results[0].copy(isSaved = true, isSubscribed= true))

    }
}

