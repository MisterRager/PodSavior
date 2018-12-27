package es.lolrav.podsavior.view.addseries


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import dagger.android.support.AndroidSupportInjection
import es.lolrav.podsavior.R
import es.lolrav.podsavior.data.ext.toLiveData
import es.lolrav.podsavior.view.addseries.view.AddSeriesAdapter
import es.lolrav.podsavior.view.addseries.viewmodel.AddSeriesViewModel
import javax.inject.Inject

class AddSeriesFragment : Fragment() {
    @Inject
    lateinit var viewModel: AddSeriesViewModel

    @Inject
    lateinit var adapter: AddSeriesAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? =
            inflater.inflate(R.layout.fragment_add_series, container, false).apply {
                findViewById<EditText>(R.id.edittext_search).let { searchBox ->

                }
            }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }
}