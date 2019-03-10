package es.lolrav.podsavior.view.series.viewmodel

import androidx.lifecycle.LiveData
import es.lolrav.podsavior.android.toLiveData
import es.lolrav.podsavior.database.dao.SeriesDao
import es.lolrav.podsavior.database.entity.Series

class SeriesHomeViewModel(
        seriesUid: String,
        seriesDao: SeriesDao
) {
    val series: LiveData<Series> by lazy { seriesDao.findByUid(seriesUid).toLiveData() }
}