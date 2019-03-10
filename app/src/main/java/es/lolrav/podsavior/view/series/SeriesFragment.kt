package es.lolrav.podsavior.view.series

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import es.lolrav.podsavior.view.series.view.SeriesHome

class SeriesFragment : Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = SeriesHome(context!!).also { view ->
    }
}