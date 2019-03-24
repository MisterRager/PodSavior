package es.lolrav.podsavior.di.module

import dagger.Module
import dagger.Provides
import dagger.Reusable
import es.lolrav.podsavior.data.*
import es.lolrav.podsavior.data.merge.MergeEpisode
import es.lolrav.podsavior.data.merge.MergeSeries
import es.lolrav.podsavior.database.entity.Episode
import es.lolrav.podsavior.database.entity.Series

@Module
object DataProcessingModule {
    @[JvmStatic Provides Reusable]
    fun providesSeriesMatchAndMerge(merger: MergeSeries): MatchAndMerge<Series> = merger

    @[JvmStatic Provides Reusable]
    fun providesSeriesSerialInsertItem(merger: MatchAndMerge<Series>): SerialInsertItem<Series> =
            SerialInsertItem(merger)

    @[JvmStatic Provides Reusable]
    fun providesSeriesInsertItem(insertion: SerialInsertItem<Series>): OverlayInsertItem<Series> =
            insertion

    @[JvmStatic Provides Reusable]
    fun providesSeriesInsertionOverlayProcessor(
            insertion: OverlayInsertItem<Series>
    ): InsertionOverlayProcessor<Series> = InsertionOverlayProcessor(insertion)

    @[JvmStatic Provides Reusable]
    fun providesSeriesOverlayProcessor(
            insertionProcessor: InsertionOverlayProcessor<Series>
    ): OverlayProcessor<Series> = insertionProcessor

    @[JvmStatic Provides Reusable]
    fun providesEpisodeMatchAndMerge(merger: MergeEpisode): MatchAndMerge<Episode> = merger

    @[JvmStatic Provides Reusable]
    fun providesEpisodeSerialInsertItem(merger: MatchAndMerge<Episode>): SerialInsertItem<Episode> =
            SerialInsertItem(merger)

    @[JvmStatic Provides Reusable]
    fun providesEpisodeInsertItem(insertion: SerialInsertItem<Episode>): OverlayInsertItem<Episode> =
            insertion

    @[JvmStatic Provides Reusable]
    fun providesEpisodeInsertionOverlayProcessor(
            insertion: OverlayInsertItem<Episode>
    ): InsertionOverlayProcessor<Episode> = InsertionOverlayProcessor(insertion)

    @[JvmStatic Provides Reusable]
    fun providesEpisodeOverlayProcessor(
            insertionProcessor: InsertionOverlayProcessor<Episode>
    ): OverlayProcessor<Episode> = insertionProcessor
}