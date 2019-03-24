package es.lolrav.podsavior.view.series.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import es.lolrav.podsavior.view.series.SeriesFragment

@Subcomponent(modules = [SeriesModule::class])
interface SeriesComponent {
    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun uid(@SeriesUid seriesUid: String): Builder
        @BindsInstance
        fun fragment(seriesFragment: Fragment): Builder
        fun build(): SeriesComponent
    }

    fun inject(seriesFragment: SeriesFragment)
}