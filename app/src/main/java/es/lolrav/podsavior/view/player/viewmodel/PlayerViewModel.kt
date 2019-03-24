package es.lolrav.podsavior.view.player.viewmodel

import android.app.Application
import android.text.Html
import androidx.lifecycle.*
import dagger.Reusable
import es.lolrav.podsavior.android.toLiveData
import es.lolrav.podsavior.data.ext.map
import es.lolrav.podsavior.data.ext.switchMap
import es.lolrav.podsavior.database.dao.EpisodeDao
import es.lolrav.podsavior.database.dao.ProgressDao
import es.lolrav.podsavior.database.entity.Episode
import es.lolrav.podsavior.database.entity.Progress
import es.lolrav.podsavior.di.qualifiers.EpisodeUid
import io.reactivex.Flowable
import org.threeten.bp.Duration
import javax.inject.Inject

class PlayerViewModel(
        application: Application,
        episodeUid: String,
        episodeDao: EpisodeDao,
        progressDao: ProgressDao
) : AndroidViewModel(application) {
    private val episode: Flowable<Episode> by lazy {
        episodeDao.getByUid(episodeUid).replay(1).refCount()
    }

    private val progress: Flowable<Progress> by lazy {
        episode.map(Episode::uid)
                .distinctUntilChanged()
                .switchMap(progressDao::getByEpisodeUid)
                .replay(1).refCount()
    }

    val title: LiveData<String> by episodeLazy(Episode::name)

    private val descriptionPlain: LiveData<String?> by episodeLazy(Episode::description)
    private val descriptionMarkup: LiveData<String?> by episodeLazy(Episode::descriptionMarkup)

    val description: LiveData<CharSequence> by lazy {
        descriptionMarkup.switchMap { desc ->
            desc?.let(Html::fromHtml)
                    ?.let { MutableLiveData<CharSequence>().apply { setValue(it as CharSequence) } }
                    ?: descriptionMarkup.map { (it ?: "") as CharSequence }
        }
    }

    val image: LiveData<String?> by episodeLazy(Episode::imageUri)
    val streamUri: LiveData<String> by episodeLazy(Episode::episodeUri)
    val duration: LiveData<Duration> by episodeLazy(Episode::duration)

    val currentPosition: LiveData<Duration> by lazy {
        progress.map(Progress::duration).distinctUntilChanged().toLiveData()
    }

    private fun <T> episodeLazy(fn: Episode.() -> T): Lazy<LiveData<T>> =
            lazy { episode.map(fn).distinctUntilChanged().toLiveData() }

    @Reusable
    class Builder
    @Inject
    constructor(
            private val application: Application,
            @EpisodeUid private val episodeUid: String,
            private val episodeDao: EpisodeDao,
            private val progressDao: ProgressDao
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass == PlayerViewModel::class.java) {
                @Suppress("UNCHECKED_CAST")
                return PlayerViewModel(
                        application,
                        episodeUid,
                        episodeDao,
                        progressDao) as T
            }
            throw UnsupportedOperationException(
                    "Cannot construct unknown class ${modelClass.simpleName}")
        }

    }
}