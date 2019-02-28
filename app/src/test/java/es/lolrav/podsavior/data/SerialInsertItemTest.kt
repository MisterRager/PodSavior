package es.lolrav.podsavior.data

import dagger.Component
import es.lolrav.podsavior.database.entity.Series
import es.lolrav.podsavior.di.TestMatchAndMergeModule
import es.lolrav.podsavior.di.TestSeriesModule
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
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
    lateinit var serialInsertItem: SerialInsertItem<Series>

    lateinit var seriesList: List<Series>

    @Before
    fun setup() {
        DaggerSerialInsertItemTestComponent.builder().build().inject(this)
        seriesList = listOf(seriesA, seriesB, seriesC, seriesD)
    }

    @Test
    fun testMatchAndMergeFirst() {
        val replacement = seriesA.copy(name = "updated")
        val update = serialInsertItem.insert(seriesList, replacement)

        assertEquals(seriesList.size, update.size)
        assertEquals(replacement, update[0])
        assertEquals(seriesList[1], update[1])
        assertEquals(seriesList[2], update[2])
        assertEquals(seriesList[3], update[3])
    }

    @Test
    fun testMatchAndMergeSecond() {
        val replacement = seriesB.copy(name = "updated")
        val update = serialInsertItem.insert(seriesList, replacement)

        assertEquals(seriesList.size, update.size)
        assertEquals(seriesList[0], update[0])
        assertEquals(replacement, update[1])
        assertEquals(seriesList[2], update[2])
        assertEquals(seriesList[3], update[3])
    }

    @Test
    fun testMatchAndMergeThird() {
        val replacement = seriesC.copy(name = "updated")
        val update = serialInsertItem.insert(seriesList, replacement)

        assertEquals(seriesList.size, update.size)
        assertEquals(seriesList[0], update[0])
        assertEquals(seriesList[1], update[1])
        assertEquals(replacement, update[2])
        assertEquals(seriesList[3], update[3])
    }

    @Test
    fun testMatchAndMergeLast() {
        val replacement = seriesD.copy(name = "updated")
        val update = serialInsertItem.insert(seriesList, replacement)

        assertEquals(seriesList.size, update.size)
        assertEquals(seriesList[0], update[0])
        assertEquals(seriesList[1], update[1])
        assertEquals(seriesList[2], update[2])
        assertEquals(replacement, update[3])
    }
}

@Component(modules = [TestSeriesModule::class, TestMatchAndMergeModule::class])
interface SerialInsertItemTestComponent {
    @Component.Builder
    interface Builder {
        fun build(): SerialInsertItemTestComponent
    }

    fun inject(test: SerialInsertItemTest)
}