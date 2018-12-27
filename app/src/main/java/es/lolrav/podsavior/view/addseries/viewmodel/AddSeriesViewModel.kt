package es.lolrav.podsavior.view.addseries.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import es.lolrav.podsavior.data.CompositeItemSource
import es.lolrav.podsavior.data.ItemSource
import es.lolrav.podsavior.data.ext.switchMap
import es.lolrav.podsavior.database.entity.Series
import javax.inject.Inject

class AddSeriesViewModel
@Inject
constructor(seriesSources: Set<ItemSource<Series>>) : ViewModel() {
    private val compositeSource: ItemSource<Series> =
            CompositeItemSource(*seriesSources.toTypedArray())

    val searchQuery: MutableLiveData<String> = MutableLiveData()

    val seriesList: LiveData<List<Series>> =
            searchQuery.switchMap {
                LiveDataReactiveStreams.fromPublisher(compositeSource.findByName(it))
            }
}