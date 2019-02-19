package es.lolrav.podsavior.view.addseries


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import es.lolrav.podsavior.R
import es.lolrav.podsavior.android.onTextIsUpdated
import es.lolrav.podsavior.android.toLiveData
import es.lolrav.podsavior.di.has.appComponent
import es.lolrav.podsavior.view.addseries.di.AddSeriesComponent
import es.lolrav.podsavior.view.addseries.di.HasAddSeriesComponent
import es.lolrav.podsavior.view.addseries.view.AddSeriesAdapter
import es.lolrav.podsavior.view.addseries.viewmodel.AddSeriesViewModel
import es.lolrav.podsavior.view.observer.UpdateViewWhenTextIsDifferent
import javax.inject.Inject

class AddSeriesFragment : Fragment(), HasAddSeriesComponent {
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
                    // Set search box to viewmodel state
                    viewModel.searchQuery
                            .observe(viewLifecycleOwner, UpdateViewWhenTextIsDifferent(searchBox))

                    searchBox.onTextIsUpdated
                            .toLiveData()
                            .observe(viewLifecycleOwner, Observer {
                                viewModel.searchQuery.value = it
                            })
                }

                findViewById<RecyclerView>(R.id.recyclerview_series).also { recycler ->
                    recycler.layoutManager = LinearLayoutManager(context)
                    recycler.adapter = adapter
                }

                viewModel.seriesList.observe(viewLifecycleOwner, Observer { seriesList ->
                    adapter.items = seriesList.toMutableList()
                })
            }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addSeriesComponent.inject(this)
    }

    override val addSeriesComponent: AddSeriesComponent by lazy {
        context!!.appComponent!!.buildSeriesComponent()
                .build()
    }
}