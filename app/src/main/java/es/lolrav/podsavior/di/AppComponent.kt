package es.lolrav.podsavior.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import es.lolrav.podsavior.view.addseries.di.AddSeriesComponent
import es.lolrav.podsavior.view.home.di.HomeComponent
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

    fun buildSeriesComponent(): AddSeriesComponent.Builder
    fun buildHomeComponent(): HomeComponent.Builder
}