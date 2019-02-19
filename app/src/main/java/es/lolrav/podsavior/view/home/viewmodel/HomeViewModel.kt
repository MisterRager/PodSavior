package es.lolrav.podsavior.view.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import es.lolrav.podsavior.android.toLiveData
import es.lolrav.podsavior.database.dao.SeriesDao
import es.lolrav.podsavior.database.entity.Series
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeViewModel
@Inject
constructor(
        seriesDao: SeriesDao
) : ViewModel() {
    val subscriptions: LiveData<List<Series>> by lazy {
        seriesDao.findAllSubscriptions().toLiveData()
    }
}