package es.lolrav.podsavior.view.series.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import es.lolrav.podsavior.android.toLiveData
import es.lolrav.podsavior.database.dao.EpisodeDao
import es.lolrav.podsavior.database.dao.SeriesDao
import es.lolrav.podsavior.database.entity.Episode
import es.lolrav.podsavior.database.entity.Series
import es.lolrav.podsavior.gretchen.FetchFeed
import es.lolrav.podsavior.view.series.di.SeriesScope
import es.lolrav.podsavior.view.series.di.SeriesUid
import javax.inject.Inject

@SeriesScope
class SeriesViewModel
@Inject
constructor(
        application: Application,
        @SeriesUid private val seriesUid: String,
        seriesDao: SeriesDao,
        episodeDao: EpisodeDao
) : AndroidViewModel(application) {
    val series: LiveData<Series> by lazy {
        seriesDao.findByUid(seriesUid)
                .map(List<Series>::first)
                .toLiveData()
    }

    val episodes: LiveData<List<Episode>> by lazy { episodeDao.getBySeries(seriesUid).toLiveData() }

    fun manuallyRefreshSeries() {
        FetchFeed.fetchSeries(getApplication(), seriesUid)
    }
}