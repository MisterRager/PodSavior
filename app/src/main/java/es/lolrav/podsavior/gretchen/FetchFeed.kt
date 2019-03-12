package es.lolrav.podsavior.gretchen

import android.app.IntentService
import android.content.Context
import android.content.Intent
import androidx.work.WorkManager
import es.lolrav.podsavior.database.dao.SeriesDao
import es.lolrav.podsavior.di.has.appComponent
import es.lolrav.podsavior.gretchen.jobs.UpdateSeriesFromRss
import javax.inject.Inject

class FetchFeed : IntentService("FetchFeed") {
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
        }
    }

    private fun updateSeries(uid: String) {
        workManager.enqueue(
                UpdateSeriesFromRss.setupRequestBuilder(uid)
                        .setInputMerger(UpdateSeriesFromRss.InputMerger::class.java)
                        .build())
    }

    companion object {
        const val ACTION_UPDATE_SERIES_FEED = "es.lolrav.podsavior.gretchen.action.GET_ONE"

        const val SERIES_UID = "es.lolrav.podsavior.gretchen.extra.SERIES_UID"

        fun fetchSeries(context: Context, seriesUid: String) {
            Intent(context, FetchFeed::class.java)
                    .apply {
                        putExtra(SERIES_UID, seriesUid)
                        action = ACTION_UPDATE_SERIES_FEED
                    }
                    .let(context::startService)
        }
    }
}
