package es.lolrav.podsavior.view.series.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Reusable
import es.lolrav.podsavior.android.toLiveData
import es.lolrav.podsavior.database.dao.EpisodeDao
import es.lolrav.podsavior.database.dao.SeriesDao
import es.lolrav.podsavior.database.entity.Episode
import es.lolrav.podsavior.database.entity.Series
import es.lolrav.podsavior.gretchen.WorkDispatcher
import es.lolrav.podsavior.di.qualifiers.SeriesUid
import java.lang.UnsupportedOperationException
import javax.inject.Inject

class SeriesViewModel(
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
        WorkDispatcher.fetchSeries(getApplication(), seriesUid)
    }

    fun downloadEpisode(episodeUid: String) =
            WorkDispatcher.fetchEpisode(getApplication(), episodeUid)

    @Reusable
    class Builder
    @Inject
    constructor(
            private val application: Application,
            @SeriesUid private val seriesUid: String,
            private val seriesDao: SeriesDao,
            private val episodeDao: EpisodeDao
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass == SeriesViewModel::class.java) {
                // We checked above
                @Suppress("UNCHECKED_CAST")
                return SeriesViewModel(application, seriesUid, seriesDao, episodeDao) as T
            }
            throw UnsupportedOperationException(
                    "Cannot construct unknown class ${modelClass.simpleName}")
        }
    }
}