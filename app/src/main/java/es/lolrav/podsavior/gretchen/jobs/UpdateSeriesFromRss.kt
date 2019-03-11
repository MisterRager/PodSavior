package es.lolrav.podsavior.gretchen.jobs

import android.content.Context
import android.net.Uri
import androidx.work.*
import com.google.common.util.concurrent.ListenableFuture
import es.lolrav.podsavior.data.rx.RxListenableFuture
import es.lolrav.podsavior.data.xml.FeedParser
import es.lolrav.podsavior.data.xml.Or
import es.lolrav.podsavior.database.dao.EpisodeDao
import es.lolrav.podsavior.database.dao.SeriesDao
import es.lolrav.podsavior.database.entity.Episode
import es.lolrav.podsavior.database.entity.Series
import es.lolrav.podsavior.di.has.appComponent
import io.reactivex.*
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
                .flatMap { series ->
                    fetchFeed(requestBuilder(series.feedUri).build())
                            .map(Pair<Call, Response>::second)
                            .flatMapMaybe { response -> Maybe.fromCallable { response.body() } }
                            .map { body -> body.source().inputStream() }
                            .map(FeedParser(parser, series)::parse)
                            .flatMapObservable { seriesOrEpisodes ->
                                Observable
                                        .create<Or<Series, Episode>> { emitter ->
                                            seriesOrEpisodes.forEach { or ->
                                                emitter.onNext(or)
                                            }
                                        }
                                        .publish { orStream ->
                                            Completable.mergeArray(
                                                    orStream
                                                            .filter { (series, _) -> series != null }
                                                            .map { (series, _) -> series!! }
                                                            .flatMapCompletable { seriesDao.save(it) },
                                                    orStream
                                                            .filter { (_, episode) -> episode != null }
                                                            .map { (_, episode) -> episode!! }
                                                            .window(30)
                                                            .flatMapCompletable {
                                                                it.toList()
                                                                        .map(List<Episode>::toTypedArray)
                                                                        .flatMapCompletable(episodeDao::save)
                                                            }
                                            ).toObservable<Any>().materialize()
                                        }
                            }
                            .toFlowable(BackpressureStrategy.BUFFER)
                }
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

    private fun fetchFeed(request: Request): Single<Pair<Call, Response>> =
            Single.using<Pair<Call, Response>, Call>(
                    { okHttp.newCall(request) },
                    { call ->
                        Single.create { emitter -> call.enqueue(SingleCallback(emitter)) }
                    },
                    Call::cancel)

    private fun requestBuilder(feedUri: String): Request.Builder =
            Request.Builder()
                    .get()
                    .url(forceHttps(feedUri))

    private fun forceHttps(feedUri: String): String =
            Uri.parse(feedUri).let { uri ->
                if (uri.scheme == "http") {
                    Uri.parse(uri.toString().replace("http", "https"))
                            .toString()
                } else {
                    feedUri
                }
            }

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