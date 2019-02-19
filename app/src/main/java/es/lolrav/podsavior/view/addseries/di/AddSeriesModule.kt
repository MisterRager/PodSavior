package es.lolrav.podsavior.view.addseries.di

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import es.lolrav.podsavior.data.CompositeItemSource
import es.lolrav.podsavior.data.ItemSource
import es.lolrav.podsavior.database.dao.SeriesDao
import es.lolrav.podsavior.database.entity.Series
import es.lolrav.podsavior.net.itunes.ITunesSeriesSource
import es.lolrav.podsavior.net.itunes.ITunesService
import es.lolrav.podsavior.view.addseries.viewmodel.RootSeriesSource

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
    fun providesITunesSeriesSource(
            service: ITunesService
    ): ItemSource<Series> = ITunesSeriesSource(service) { itunes ->
        Series(
                uid = "itunes_series#${itunes.id}",
                name = itunes.name,
                feedUri = itunes.rssFeedUrl ?: "",
                description = "",
                isSubscribed = false
        )
    }

    @[JvmStatic Provides]
    fun providesCompositeSeriesSource(
            seriesSources: Set<@JvmSuppressWildcards ItemSource<@JvmSuppressWildcards Series>>
    ): CompositeItemSource<Series> = CompositeItemSource(*seriesSources.toTypedArray()) { current, update ->
        current + update
    }

    @[JvmStatic Provides RootSeriesSource]
    fun providesRootSeriesSource(
            compositeSeriesSource: CompositeItemSource<Series>
    ): ItemSource<Series> = compositeSeriesSource
}