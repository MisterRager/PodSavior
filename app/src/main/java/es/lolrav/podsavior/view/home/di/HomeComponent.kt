package es.lolrav.podsavior.view.home.di

import dagger.Subcomponent
import es.lolrav.podsavior.view.home.HomeFragment

@Subcomponent(modules = [HomeModule::class])
interface HomeComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): HomeComponent
    }

    fun inject(homeFragment: HomeFragment)
}

interface HasHomeComponent {
    val homeComponent: HomeComponent
}