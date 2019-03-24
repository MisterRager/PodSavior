package es.lolrav.podsavior.view.series

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import dagger.Lazy
import es.lolrav.podsavior.di.has.appComponent
import es.lolrav.podsavior.view.series.di.SeriesComponent
import es.lolrav.podsavior.view.series.view.EpisodeAdapter
import es.lolrav.podsavior.view.series.view.SeriesHome
import es.lolrav.podsavior.view.series.viewmodel.SeriesViewModel
import javax.inject.Inject

class SeriesFragment : Fragment() {
    @Inject
    lateinit var viewModel: SeriesViewModel

    @Inject
    lateinit var picasso: Picasso

    @Inject
    lateinit var adapter: EpisodeAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        component.inject(this)
        viewModel.manuallyRefreshSeries()
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = SeriesHome(context!!).also { view ->
        viewModel.series.observe(viewLifecycleOwner, Observer { series ->
            view.title.text = series.name
            view.description.text = series.description
            if (!series.iconPath.isNullOrBlank()) {
                picasso.load(series.iconPath).into(view.coverImage)
            }

            view.episodes.adapter = adapter
            view.episodes.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

            viewModel.episodes.observe(viewLifecycleOwner, Observer { episodes ->
                adapter.setEpisodes(episodes)
                adapter.notifyDataSetChanged()
            })
        })
    }

    private val seriesUid: String get() = arguments!!.getString("series_uid")!!

    private val component: SeriesComponent by lazy {
        context!!.appComponent!!.buildSeriesComponent()
                .uid(seriesUid)
                .build()
    }
}