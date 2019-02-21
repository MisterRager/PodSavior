package es.lolrav.podsavior.data

import es.lolrav.podsavior.database.entity.Series
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class SerialInsertItemTest {
    @Mock
    lateinit var seriesA: Series
    @Mock
    lateinit var seriesB: Series
    @Mock
    lateinit var seriesC: Series
    @Mock
    lateinit var seriesD: Series

    private lateinit var seriesList: List<Series>
    private lateinit var matchAndMerge: MatchAndMerge<Series>
    private lateinit var serialInsertItem: SerialInsertItem<Series>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        matchAndMerge = Mockito.spy(TestMerger())
        seriesList = listOf(seriesA, seriesB, seriesC, seriesD)
        serialInsertItem = SerialInsertItem(matchAndMerge)
    }

    @Test
    fun testMatchFirstAndMergeFirst() {
    }

    internal class TestMerger : MatchAndMerge<Series> {
        override fun matches(left: Series, right: Series): Boolean = left === right
        override fun merge(bottom: Series, top: Series): Series = top
    }
}