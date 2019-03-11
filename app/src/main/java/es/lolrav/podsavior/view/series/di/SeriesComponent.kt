package es.lolrav.podsavior.view.series.di

import dagger.BindsInstance
import dagger.Subcomponent
import es.lolrav.podsavior.view.series.SeriesFragment
import javax.inject.Singleton

@SeriesScope
@Subcomponent(modules = [SeriesModule::class])
interface SeriesComponent {
    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun uid(@SeriesUid seriesUid: String): Builder
        fun build(): SeriesComponent
    }

    fun inject(seriesFragment: SeriesFragment)
}