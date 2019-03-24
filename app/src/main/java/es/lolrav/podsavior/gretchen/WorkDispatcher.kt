package es.lolrav.podsavior.gretchen

import android.app.IntentService
import android.content.Context
import android.content.Intent
import androidx.work.WorkManager
import es.lolrav.podsavior.database.dao.SeriesDao
import es.lolrav.podsavior.di.has.appComponent
import es.lolrav.podsavior.gretchen.jobs.DownloadEpisode
import es.lolrav.podsavior.gretchen.jobs.UpdateSeriesFromRss
import javax.inject.Inject

class WorkDispatcher : IntentService("WorkDispatcher") {
    @Inject
    lateinit var seriesDao: SeriesDao

    @Inject
    lateinit var workManager: WorkManager

    override fun onCreate() {
        super.onCreate()
        application.appComponent.inject(this)
    }

    override fun onHandleIntent(intent: Intent?) {
        when (intent?.action) {
            ACTION_UPDATE_SERIES_FEED -> updateSeries(intent.getStringExtra(SERIES_UID))
            ACTION_DOWNLOAD_EPISODE -> downloadEpisode(intent.getStringExtra(EPISODE_UID))
        }
    }

    private fun updateSeries(uid: String) {
        workManager.enqueue(UpdateSeriesFromRss.buildRequest(uid))
    }

    private fun downloadEpisode(uid: String) {
        workManager.enqueue(DownloadEpisode.buildRequest(uid))
    }

    companion object {
        const val ACTION_UPDATE_SERIES_FEED = "es.lolrav.podsavior.gretchen.action.GET_ONE"
        const val ACTION_DOWNLOAD_EPISODE = "es.lolrav.podsavior.gretchen.action.DOWNLOAD_SHOW"

        const val SERIES_UID = "es.lolrav.podsavior.gretchen.extra.SERIES_UID"
        const val EPISODE_UID = "es.lolrav.podsavior.gretchen.extra.EPISODE_UID"

        fun fetchSeries(context: Context, seriesUid: String) {
            Intent(context, WorkDispatcher::class.java)
                    .apply {
                        putExtra(SERIES_UID, seriesUid)
                        action = ACTION_UPDATE_SERIES_FEED
                    }
                    .let(context::startService)
        }

        fun fetchEpisode(context: Context, episodeUid: String) {
            Intent(context, WorkDispatcher::class.java)
                    .apply {
                        putExtra(EPISODE_UID, episodeUid)
                        action = ACTION_DOWNLOAD_EPISODE
                    }
                    .let(context::startService)
        }
    }
}
