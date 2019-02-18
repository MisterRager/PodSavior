package es.lolrav.podsavior.view.addseries.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import es.lolrav.podsavior.data.ItemSource
import es.lolrav.podsavior.data.ext.switchMap
import es.lolrav.podsavior.database.entity.Series
import javax.inject.Inject
import javax.inject.Qualifier

class AddSeriesViewModel
@Inject
constructor(@RootSeriesSource seriesSource: ItemSource<Series>) : ViewModel() {
    val searchQuery: MutableLiveData<CharSequence> = MutableLiveData()

    val seriesList: LiveData<List<Series>> =
            searchQuery.switchMap {
                LiveDataReactiveStreams.fromPublisher(seriesSource.findByName(it))
            }
}

@Qualifier
annotation class RootSeriesSource