package es.lolrav.podsavior.gretchen.jobs

import android.content.Context
import androidx.work.*
import com.google.common.util.concurrent.ListenableFuture
import es.lolrav.podsavior.data.rx.RxListenableFuture
import es.lolrav.podsavior.data.xml.FeedParser
import es.lolrav.podsavior.database.dao.EpisodeDao
import es.lolrav.podsavior.database.dao.SeriesDao
import es.lolrav.podsavior.database.entity.Episode
import es.lolrav.podsavior.di.has.appComponent
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.internal.functions.Functions
import okhttp3.*
import org.xmlpull.v1.XmlPullParser
import java.io.IOException
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

class UpdateSeriesFromRss(
        context: Context,
        private val workerParams: WorkerParameters
) : ListenableWorker(context, workerParams) {

    private val isInjected: AtomicBoolean = AtomicBoolean(false)

    @Inject
    lateinit var seriesDao: SeriesDao
    @Inject
    lateinit var episodeDao: EpisodeDao
    @Inject
    lateinit var okHttp: OkHttpClient
    @Inject
    lateinit var parser: XmlPullParser

    override fun startWork(): ListenableFuture<Result> {
        // Make sure DI is done
        if (isInjected.compareAndSet(false, true)) {
            (applicationContext.appComponent)!!.inject(this)
        }

        return (seriesUids.let(seriesDao::findByUid))
                .take(1) // Don't need updates, just doing one
                .flatMapIterable(Functions.identity())
                .flatMapCompletable { series ->
                    fetchFeed(series.feedUri)
                            .map(Pair<Call, Response>::second)
                            .flatMapMaybe { response -> Maybe.fromCallable { response.body() } }
                            .map { body -> body.source().inputStream() }
                            .map(FeedParser(parser, series)::parse)
                            .flatMapCompletable { (seriesSequence, episodeSequence) ->
                                Completable.defer {
                                    seriesSequence.firstOrNull()?.let { newSeries ->
                                        seriesDao.save(newSeries)
                                    } ?: Completable.complete()
                                }.mergeWith(
                                        Completable.merge(
                                                episodeSequence.windowed(30)
                                                        .map(List<Episode>::toTypedArray)
                                                        .map(episodeDao::save)
                                                        .toList()))
                            }
                }
                .toObservable<Any>()
                .materialize()
                .map { notification ->
                    when {
                        notification.isOnComplete -> ListenableWorker.Result.success()
                        notification.isOnError -> ListenableWorker.Result.failure()
                        else -> ListenableWorker.Result.failure()
                    }
                }
                .first(ListenableWorker.Result.failure())
                .let(::RxListenableFuture)
    }

    private fun fetchFeed(feedUri: String): Single<Pair<Call, Response>> =
            Single.using<Pair<Call, Response>, Call>(
                    {
                        Request.Builder()
                                .get()
                                .url(feedUri)
                                .build()
                                .let(okHttp::newCall)
                    },
                    { call ->
                        Single.create { emitter -> call.enqueue(SingleCallback(emitter)) }
                    },
                    Call::cancel)

    private val seriesUids: Array<String>
        get() = workerParams.inputData.getStringArray(ARG_SERIES_UID) ?: emptyArray()

    internal class SingleCallback(
            private val emitter: SingleEmitter<Pair<Call, Response>>
    ) : Callback {
        override fun onFailure(call: Call, e: IOException) {
            emitter.onError(OkError(call, e))
        }

        override fun onResponse(call: Call, response: Response) {
            emitter.onSuccess(call to response)
        }
    }

    class InputMerger : androidx.work.InputMerger() {
        override fun merge(inputs: MutableList<Data>): Data =
                inputs
                        .asSequence()
                        .flatMap {
                            (it.getStringArray(ARG_SERIES_UID) ?: emptyArray()).asSequence()
                        }
                        .toList()
                        .toTypedArray()
                        .let(UpdateSeriesFromRss.Companion::buildData)
    }

    internal class OkError(val call: Call, e: IOException) : IOException(e)

    companion object {
        const val ARG_SERIES_UID = "es.lolrav.podsavior.gretchen.jobs.SERIES_UID"

        fun buildData(vararg seriesUid: String): Data =
                Data.Builder()
                        .putStringArray(ARG_SERIES_UID, seriesUid)
                        .build()


        fun setupRequestBuilder(vararg seriesUid: String): OneTimeWorkRequest.Builder =
                buildData(*seriesUid)
                        .let(OneTimeWorkRequestBuilder<UpdateSeriesFromRss>()::setInputData)
    }
}