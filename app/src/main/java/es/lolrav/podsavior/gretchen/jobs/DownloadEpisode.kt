package es.lolrav.podsavior.gretchen.jobs

import android.content.Context
import android.util.Base64
import android.util.Log
import androidx.work.*
import com.google.common.util.concurrent.ListenableFuture
import es.lolrav.podsavior.data.rx.RxListenableFuture
import es.lolrav.podsavior.database.dao.EpisodeDao
import es.lolrav.podsavior.database.entity.Episode
import es.lolrav.podsavior.di.has.appComponent
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import okhttp3.*
import okio.Okio
import java.io.File
import java.io.IOException
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

private val TAG = DownloadEpisode::class.java.simpleName

class DownloadEpisode(
        appContext: Context,
        private val workerParams: WorkerParameters
) : ListenableWorker(appContext, workerParams) {

    private val isInjected: AtomicBoolean = AtomicBoolean(false)

    @Inject
    lateinit var episodeDao: EpisodeDao

    @Inject
    lateinit var okhttp: OkHttpClient

    override fun startWork(): ListenableFuture<Result> {
        if (isInjected.compareAndSet(false, true)) {
            applicationContext.appComponent!!.inject(this)
        }

        return episodeDownloads
                .toSingleDefault(ListenableWorker.Result.success())
                .onErrorReturnItem(ListenableWorker.Result.failure())
                .let(::RxListenableFuture)
    }

    private val episodeUids: Array<String> by lazy {
        workerParams.inputData.getStringArray(ARG_EPISODE_UID) ?: emptyArray()
    }

    private val episodeStream: Flowable<Episode> by lazy {
        episodeDao.getListByUid(*(episodeUids.distinct().toTypedArray()))
                .take(1)
                .flatMapIterable { it }
    }

    private val episodeDownloads: Completable by lazy {
        episodeStream
                .flatMapCompletable(this::downloadEpisode)
                .doOnError { Log.w(TAG, "Error Downloading Episode!!", it) }
    }

    private fun downloadEpisode(episode: Episode): Completable =
            Completable.using(
                    {
                        Request.Builder()
                                .url(episode.episodeUri)
                                .get()
                                .build()
                                .let(okhttp::newCall)
                    },
                    { call ->
                        Single
                                .create<Response> { emitter ->
                                    call.enqueue(object : Callback {
                                        override fun onResponse(call: Call, response: Response) {
                                            emitter.onSuccess(response)
                                        }

                                        override fun onFailure(call: Call, e: IOException) {
                                            emitter.onError(e)
                                        }
                                    })
                                }
                                .flatMapCompletable { res -> atomicFileDownload(episode, res) }
                    },
                    Call::cancel)

    private fun atomicFileDownload(
            episode: Episode,
            response: Response
    ): Completable =
            episode.onDiskFile.let { target ->
                episodeDao
                        .updateFilePathIfBlank(episode.uid, target.absolutePath)
                        .filter { it > 0 }
                        .flatMapCompletable {
                            streamFile(
                                    response,
                                    target
                                            .apply { Log.v(TAG, "Downloading to $canonicalPath") }
                                            .apply { parentFile.mkdirs() }
                                            .apply { createNewFile() }
                            )
                        }
            }


    private fun streamFile(response: Response, target: File): Completable =
            Completable
                    .fromAction {
                        Log.v(TAG, "Downloading (${response.request().url()}) to (${target.path})")
                        response.body()?.let {
                            it.source().let(Okio.buffer(Okio.sink(target))::writeAll)
                            it.close()
                        }
                    }

    private val Episode.onDiskFile: File
        get() =
            when {
                onDiskPath != null -> File(onDiskPath)
                else -> File(
                        applicationContext.filesDir,
                        "/${seriesUid.hashify()}/${uid.hashify()}/${UUID.randomUUID()}")
            }

    private fun String.hashify(): String =
            Base64.encodeToString(this.toByteArray(), Base64.DEFAULT)
                    .let { str -> str.substring(0, Math.min(str.length, 20)) }

    class MergeInput : InputMerger() {
        override fun merge(inputs: MutableList<Data>): Data =
                inputs.asSequence()
                        .flatMap {
                            it.getStringArray(ARG_EPISODE_UID)?.asSequence() ?: emptySequence()
                        }
                        .toSet()
                        .toTypedArray()
                        .let(DownloadEpisode.Companion::buildData)
    }

    companion object {
        private val ARG_EPISODE_UID: String by lazy {
            "${DownloadEpisode::class.java.canonicalName}::episodeUid"
        }

        fun buildData(vararg episodeUid: String): Data =
                Data.Builder()
                        .putStringArray(ARG_EPISODE_UID, episodeUid)
                        .build()

        private fun setupRequestBuilder(episodeUid: String): OneTimeWorkRequest.Builder =
                buildData(episodeUid)
                        .let(OneTimeWorkRequestBuilder<DownloadEpisode>()::setInputData)

        fun buildRequest(episodeUid: String): OneTimeWorkRequest =
                setupRequestBuilder(episodeUid).setInputMerger(MergeInput::class).build()
    }
}