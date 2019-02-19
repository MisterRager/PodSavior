package es.lolrav.podsavior.view.addseries.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import es.lolrav.podsavior.data.ItemSource
import es.lolrav.podsavior.data.ext.switchMap
import es.lolrav.podsavior.database.dao.SeriesDao
import es.lolrav.podsavior.database.entity.Series
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Qualifier
import javax.inject.Singleton

class AddSeriesViewModel
constructor(
        seriesSource: ItemSource<Series>,
        private val seriesDao: SeriesDao
) : ViewModel() {
    val searchQuery: MutableLiveData<CharSequence> = MutableLiveData()

    val seriesList: LiveData<List<Series>> =
            searchQuery.switchMap {
                LiveDataReactiveStreams.fromPublisher(seriesSource.findByName(it))
            }

    fun unsubscribeTo(series: Series) {
        seriesDao.save(series.copy(isSubscribed = false, isSaved = true))
                .subscribeOn(Schedulers.io())
                .subscribe()
    }

    fun subscribeTo(series: Series) {
        seriesDao.save(series.copy(isSubscribed = true, isSaved = true))
                .subscribeOn(Schedulers.io())
                .subscribe()
    }
}

@Qualifier
annotation class RootSeriesSource