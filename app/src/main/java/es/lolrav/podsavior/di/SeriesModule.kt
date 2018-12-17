package es.lolrav.podsavior.di

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import es.lolrav.podsavior.database.dao.SeriesDao
import es.lolrav.podsavior.database.entity.Series
import es.lolrav.podsavior.data.ItemSource

@Module
class SeriesModule {
    @[Provides IntoSet]
    fun providesLocalSeriesSource(dao: SeriesDao): ItemSource<Series> =
            ItemSource.by { name ->
                dao.getAll().map { list: List<Series> ->
                    list.filter { series ->
                        series.name.contains(name)
                    }
                }
            }
}