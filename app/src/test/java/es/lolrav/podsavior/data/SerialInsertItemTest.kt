package es.lolrav.podsavior.data

import dagger.Component
import dagger.Module
import dagger.Provides
import es.lolrav.podsavior.database.entity.Series
import es.lolrav.podsavior.di.TestSeriesModule
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import javax.inject.Inject

class SerialInsertItemTest {
    @Inject
    lateinit var seriesA: Series
    @Inject
    lateinit var seriesB: Series
    @Inject
    lateinit var seriesC: Series
    @Inject
    lateinit var seriesD: Series

    @Inject
    lateinit var replacement: Series

    @Inject
    lateinit var matchAndMerge: MatchAndMerge<Series>
    @Inject
    lateinit var serialInsertItem: SerialInsertItem<Series>

    lateinit var seriesList: List<Series>

    @Before
    fun setup() {
        DaggerTestComponent.builder().build().inject(this)
        seriesList = listOf(seriesA, seriesB, seriesC, seriesD)
    }

    @Test
    fun testMatchAndMergeFirst() {
        Mockito.doReturn(true).`when`(matchAndMerge).matches(seriesA, replacement)

        val update = serialInsertItem.insert(seriesList, replacement)

        assertEquals(replacement, update[0])
        assertEquals(seriesList[1], update[1])
        assertEquals(seriesList[2], update[2])
        assertEquals(seriesList[3], update[3])
    }

    @Test
    fun testMatchAndMergeSecond() {
        Mockito.doReturn(true).`when`(matchAndMerge).matches(seriesB, replacement)

        val update = serialInsertItem.insert(seriesList, replacement)

        assertEquals(seriesList[0], update[0])
        assertEquals(replacement, update[1])
        assertEquals(seriesList[2], update[2])
        assertEquals(seriesList[3], update[3])
    }

    @Test
    fun testMatchAndMergeSecond() {
        Mockito.doReturn(true).`when`(matchAndMerge).matches(seriesB, replacement)

        val update = serialInsertItem.insert(seriesList, replacement)

        assertEquals(seriesList[0], update[0])
        assertEquals(replacement, update[1])
        assertEquals(seriesList[2], update[2])
        assertEquals(seriesList[3], update[3])
    }

    @Test
    fun testMatchAndMergeLast() {
        Mockito.doReturn(true).`when`(matchAndMerge).matches(seriesD, replacement)

        val update = serialInsertItem.insert(seriesList, replacement)

        assertEquals(seriesList[0], update[0])
        assertEquals(seriesList[1], update[1])
        assertEquals(seriesList[2], update[2])
        assertEquals(replacement, update[3])
    }
}

@Module
object TestMatchAndMergeModule {
    @[Provides JvmStatic]
    fun providesMatchAndMerge(merger: TestMerger): MatchAndMerge<Series> = Mockito.spy(merger)

    open class TestMerger
    @Inject
    constructor() : MatchAndMerge<Series> {
        override fun matches(left: Series, right: Series): Boolean = left === right
        override fun merge(bottom: Series, top: Series): Series = top
    }
}

@Component(modules = [TestSeriesModule::class, TestMatchAndMergeModule::class])
interface TestComponent {
    @Component.Builder
    interface Builder {
        fun build(): TestComponent
    }

    fun inject(test: SerialInsertItemTest)
}