package es.lolrav.podsavior.view.addseries.di

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import es.lolrav.podsavior.data.ItemSource
import es.lolrav.podsavior.database.dao.SeriesDao
import es.lolrav.podsavior.database.entity.Series
import io.reactivex.Flowable

@Module
object AddSeriesModule {
    @[JvmStatic Provides IntoSet]
    fun providesLocalSeriesSource(dao: SeriesDao): ItemSource<@JvmSuppressWildcards Series> =
            ItemSource.by { name ->
                // TODO: this in SQL instead of this lols
                dao.getAll().map { list: List<Series> ->
                    list.filter { series ->
                        series.name.contains(name)
                    }
                }
            }

    @[JvmStatic Provides IntoSet]
    fun providesDummySeriesSource(): ItemSource<@JvmSuppressWildcards Series> =
            object : ItemSource<Series> {
                override fun findByName(name: String): Flowable<List<Series>> = Flowable.empty()
            }
}