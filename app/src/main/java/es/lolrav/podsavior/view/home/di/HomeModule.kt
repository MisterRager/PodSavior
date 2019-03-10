package es.lolrav.podsavior.view.home.di

import dagger.Module
import dagger.Provides
import es.lolrav.podsavior.database.entity.Series
import es.lolrav.podsavior.view.home.view.SeriesListClickSubscription
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

@Module
object HomeModule {
    @[Provides JvmStatic SeriesListClickSubscription]
    fun providesSeriesClickSubject(): Subject<Series> = PublishSubject.create()
}