package es.lolrav.podsavior.view.player

import android.content.Context
import androidx.fragment.app.Fragment
import es.lolrav.podsavior.di.has.appComponent
import es.lolrav.podsavior.view.player.di.PlayerComponent
import es.lolrav.podsavior.view.player.viewmodel.PlayerViewModel
import javax.inject.Inject

class PlayerFragment : Fragment() {
    @Inject
    lateinit var viewModel: PlayerViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        component.inject(this)
    }

    private val episodeUid: String get() = arguments!!.getString("episode_uid")!!

    private val component: PlayerComponent by lazy {
        context!!.appComponent!!.plusPlayerComponent()
                .fragment(this)
                .uid(episodeUid)
                .build()
    }
}