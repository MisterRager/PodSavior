package es.lolrav.podsavior.view.series

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.squareup.picasso.Picasso
import es.lolrav.podsavior.di.has.appComponent
import es.lolrav.podsavior.view.series.di.SeriesComponent
import es.lolrav.podsavior.view.series.view.SeriesHome
import es.lolrav.podsavior.view.series.viewmodel.SeriesViewModel
import javax.inject.Inject

class SeriesFragment : Fragment() {
    @Inject
    lateinit var viewModel: SeriesViewModel

    @Inject
    lateinit var picasso: Picasso

    override fun onAttach(context: Context) {
        super.onAttach(context)
        component.inject(this)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = SeriesHome(context!!).also { view ->
        viewModel.series.observe(viewLifecycleOwner, Observer { series ->
            view.title.text = series.name
            view.description.text = series.description
            picasso.load(series.iconPath).into(view.coverImage)
        })
    }

    private val seriesUid: String get() = arguments!!.getString("series_uid")!!

    private val component: SeriesComponent by lazy {
        context!!.appComponent!!.buildSeriesComponent()
                .uid(seriesUid)
                .build()
    }
}