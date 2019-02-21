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

@Module
object AddSeriesModule {
    @[JvmStatic Provides IntoSet]
    fun providesLocalSeriesSource(
            dao: SeriesDao
    ): ItemSource<Series> = CallbackItemSource { dao.findByName(it.toString()) }

    @[JvmStatic Provides IntoSet]
    fun providesITunesSeriesSource(
            service: ITunesService,
            conversions: ITunesEntityConversions
    ): ItemSource<Series> = ITunesSeriesSource(service, conversions::resultToSeries)

    @[JvmStatic Provides Reusable]
    fun providesMatchAndMerge(merger: MergeSeries): MatchAndMerge<Series> = merger

    @[JvmStatic Provides Reusable]
    fun providesSerialInsertItem(merger: MatchAndMerge<Series>): SerialInsertItem<Series> =
            SerialInsertItem(merger)

    @[JvmStatic Provides Reusable]
    fun providesInsertItem(insertion: SerialInsertItem<Series>): OverlayInsertItem<Series> =
            insertion

    @[JvmStatic Provides Reusable]
    fun providesInsertionOverlayProcessor(
            insertion: OverlayInsertItem<Series>
    ): InsertionOverlayProcessor<Series> = InsertionOverlayProcessor(insertion)

    @[JvmStatic Provides Reusable]
    fun providesOverlayProcessor(
            insertionProcessor: InsertionOverlayProcessor<Series>
    ): OverlayProcessor<Series> = insertionProcessor

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