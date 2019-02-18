package es.lolrav.podsavior.view.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import es.lolrav.podsavior.R
import es.lolrav.podsavior.di.has.appComponent
import es.lolrav.podsavior.view.home.di.HasHomeComponent
import es.lolrav.podsavior.view.home.di.HomeComponent

class HomeFragment : Fragment(), HasHomeComponent {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false).apply {
        findViewById<View>(R.id.fab_add_subscription).let { addSeriesFab ->
            addSeriesFab.setOnClickListener {
                it.findNavController().navigate(R.id.addSeriesFragment)
            }
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        homeComponent.inject(this)
    }

    override val homeComponent: HomeComponent by lazy {
        context!!.appComponent!!.buildHomeComponent().build()
    }
}