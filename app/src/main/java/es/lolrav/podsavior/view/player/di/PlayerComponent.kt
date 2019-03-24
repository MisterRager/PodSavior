package es.lolrav.podsavior.view.player.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import es.lolrav.podsavior.di.qualifiers.EpisodeUid
import es.lolrav.podsavior.view.player.PlayerFragment

@Subcomponent(modules = [PlayerModule::class])
interface PlayerComponent {
    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun uid(@EpisodeUid episodeUid: String): Builder

        @BindsInstance
        fun fragment(fragment: Fragment): Builder

        fun build(): PlayerComponent
    }

    fun inject(seriesFragment: PlayerFragment)
}