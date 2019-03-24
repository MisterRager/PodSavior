package es.lolrav.podsavior.view.series.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import dagger.Reusable
import es.lolrav.podsavior.view.series.viewmodel.SeriesViewModel

@Module
object SeriesModule {
    @[JvmStatic Provides Reusable]
    fun providesViewModelProvider(
            fragment: Fragment,
            factory: SeriesViewModel.Builder
    ): ViewModelProvider = ViewModelProviders.of(fragment, factory)

    @[JvmStatic Provides Reusable]
    fun providesViewModel(
            @SeriesUid seriesUid: String,
            vmp: ViewModelProvider
    ): SeriesViewModel = vmp[seriesUid, SeriesViewModel::class.java]
}