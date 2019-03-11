package es.lolrav.podsavior.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import es.lolrav.podsavior.gretchen.FetchFeed
import es.lolrav.podsavior.gretchen.jobs.UpdateSeriesFromRss
import es.lolrav.podsavior.view.addseries.di.AddSeriesComponent
import es.lolrav.podsavior.view.home.di.HomeComponent
import es.lolrav.podsavior.view.series.di.SeriesComponent
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    RoomModule::class,
    NetModule::class
])
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun buildAddSeriesComponent(): AddSeriesComponent.Builder
    fun buildHomeComponent(): HomeComponent.Builder
    fun buildSeriesComponent(): SeriesComponent.Builder

    fun inject(fetchFeed: FetchFeed)
    fun inject(worker: UpdateSeriesFromRss)
}