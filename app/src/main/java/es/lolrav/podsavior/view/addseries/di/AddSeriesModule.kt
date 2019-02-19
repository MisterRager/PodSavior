package es.lolrav.podsavior.view.addseries.di

import dagger.Module
import dagger.Provides
import dagger.Reusable
import es.lolrav.podsavior.data.CallbackItemSource
import es.lolrav.podsavior.data.ItemSource
import es.lolrav.podsavior.data.OverlayItemSource
import es.lolrav.podsavior.database.dao.SeriesDao
import es.lolrav.podsavior.database.entity.Series
import es.lolrav.podsavior.net.itunes.ITunesSeriesSource
import es.lolrav.podsavior.net.itunes.ITunesService
import es.lolrav.podsavior.net.itunes.convert.ITunesEntityConversions
import es.lolrav.podsavior.view.addseries.viewmodel.RootSeriesSource
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers

@Module
object AddSeriesModule {
    @[JvmStatic Provides Reusable]
    fun providesLocalSeriesSource(
            dao: SeriesDao
    ): CallbackItemSource<Series> = CallbackItemSource { dao.findByName(it.toString()) }

    @[JvmStatic Provides Reusable]
    fun providesITunesSeriesSource(
            service: ITunesService,
            conversions: ITunesEntityConversions
    ): ITunesSeriesSource = ITunesSeriesSource(service, conversions::resultToSeries)

    @[JvmStatic Provides]
    fun providesCompositeSeriesSource(
            localSource: CallbackItemSource<Series>,
            iTunesSource: ITunesSeriesSource
    ): OverlayItemSource<Series> =
            OverlayItemSource(
                    Schedulers.io(),
                    setOf(localSource, iTunesSource),
                    OverlayItemSource.buildMatchAndMergeOverlayFn(
                            matchFn = { seriesA, seriesB -> seriesA.uid == seriesB.uid },
                            mergeFn = { seriesA, seriesB ->
                                Series(
                                        uid = seriesA.uid,
                                        name = seriesB.name,
                                        artistName = seriesB.artistName,
                                        feedUri = seriesB.feedUri,
                                        description = seriesB.description ?: seriesA.description,
                                        isSubscribed = (seriesB.isSaved && seriesB.isSubscribed) ||
                                                (!seriesB.isSaved &&
                                                        seriesA.isSaved &&
                                                        seriesA.isSubscribed),
                                        isSaved = seriesA.isSaved || seriesB.isSaved,
                                        iconPath = seriesB.iconPath ?: seriesA.iconPath)
                            }))

    /*
    @[JvmStatic Provides RootSeriesSource]
    fun providesRootSeriesSource(
            compositeSeriesSource: OverlayItemSource<Series>
    ): ItemSource<Series> = compositeSeriesSource
    */

    @[JvmStatic Provides RootSeriesSource]
    fun providesRootSeriesSource(): ItemSource<Series> = CallbackItemSource { Flowable.empty<List<Series>>() }
}