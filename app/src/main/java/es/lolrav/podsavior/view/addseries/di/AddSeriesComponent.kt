package es.lolrav.podsavior.view.addseries.di

import dagger.Subcomponent
import es.lolrav.podsavior.view.addseries.AddSeriesFragment

@Subcomponent(modules = [AddSeriesModule::class])
interface AddSeriesComponent {
    fun inject(addSeriesFragment: AddSeriesFragment)

    @Subcomponent.Builder
    interface Builder {
        fun fragment(addSeriesFragment: AddSeriesFragment): Builder
        fun build(): AddSeriesComponent
    }
}

interface HasAddSeriesComponent {
    val addSeriesComponent: AddSeriesComponent
}