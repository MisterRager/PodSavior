package es.lolrav.podsavior.view.addseries.di

import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.multibindings.IntoSet
import es.lolrav.podsavior.data.*
import es.lolrav.podsavior.data.merge.MergeSeries
import es.lolrav.podsavior.database.dao.SeriesDao
import es.lolrav.podsavior.database.entity.Series
import es.lolrav.podsavior.di.qualifiers.DataScheduler
import es.lolrav.podsavior.net.itunes.ITunesSeriesSource
import es.lolrav.podsavior.net.itunes.ITunesService
import es.lolrav.podsavior.net.itunes.convert.ITunesEntityConversions
import es.lolrav.podsavior.view.addseries.viewmodel.RootSeriesSource
import io.reactivex.Scheduler
import javax.inject.Named

@Module
object AddSeriesModule {
    @[JvmStatic Provides IntoSet]
    fun providesLocalSeriesSource(
            dao: SeriesDao
    ): ItemSource<Series> = CallbackItemSource { dao.findByName(it.toString()) }

    @[JvmStatic Provides Named("iTunes Source")]
    fun providesITunesSeriesSource(
            service: ITunesService,
            conversions: ITunesEntityConversions
    ): ItemSource<Series> = ITunesSeriesSource(service, conversions::resultToSeries)

    @[JvmStatic Provides IntoSet]
    fun providesSavingITunesSeriesSource(
            @Named("iTunes Source") source: ItemSource<Series>,
            dao: SeriesDao,
            merger: SerialInsertItem<Series>
    ): ItemSource<Series> = SideEffectsItemSource(source) { existingSeriesList ->
        dao.findByUid(*existingSeriesList.map(Series::uid).toTypedArray())
                .first(emptyList())
                .map { foundSeriesList: List<Series> ->
                    foundSeriesList.fold(existingSeriesList, merger::insert)
                }
                .map(List<Series>::toTypedArray)
                .flatMapCompletable(dao::save)
    }

    @[JvmStatic Provides Reusable]
    fun providesCompositeSeriesSource(
            sources: Set<@JvmSuppressWildcards ItemSource<Series>>,
            overlayProcessor: OverlayProcessor<Series>,
            @DataScheduler scheduler: Scheduler
    ): OverlayItemSource<Series> = OverlayItemSource(scheduler, sources, overlayProcessor)

    @RootSeriesSource
    @[JvmStatic Provides Reusable]
    fun providesMainSeriesSource(
            overlayItemSource: OverlayItemSource<Series>
    ): ItemSource<Series> = overlayItemSource
}