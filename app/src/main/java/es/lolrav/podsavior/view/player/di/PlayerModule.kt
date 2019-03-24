package es.lolrav.podsavior.view.player.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import dagger.Reusable
import es.lolrav.podsavior.di.qualifiers.EpisodeUid
import es.lolrav.podsavior.view.player.viewmodel.PlayerViewModel

@Module
object PlayerModule {
    @[JvmStatic Provides Reusable]
    fun providesViewModelProvider(
            fragment: Fragment,
            factory: PlayerViewModel.Builder
    ): ViewModelProvider = ViewModelProviders.of(fragment, factory)

    @[JvmStatic Provides Reusable]
    fun providesViewModel(
            @EpisodeUid episodeUid: String,
            vmp: ViewModelProvider
    ): PlayerViewModel = vmp[episodeUid, PlayerViewModel::class.java]
}