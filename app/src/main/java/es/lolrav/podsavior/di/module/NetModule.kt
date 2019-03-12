package es.lolrav.podsavior.di.module

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.Reusable
import es.lolrav.podsavior.net.itunes.ITUNES_BASE_URI
import es.lolrav.podsavior.net.itunes.ITunesService
import okhttp3.OkHttpClient
import okhttp3.Protocol
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
object NetModule {
    @[Provides JvmStatic Reusable]
    fun providesMoshi(): Moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    @[Provides JvmStatic]
    fun providesOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

    @[Provides JvmStatic Reusable]
    @Named("itunes-retrofit")
    fun providesITunesRetrofit(
            moshi: Moshi,
            okHttp: OkHttpClient
    ): Retrofit =
            Retrofit.Builder()
                    .baseUrl(ITUNES_BASE_URI)
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttp)
                    .build()

    @[Provides JvmStatic Reusable]
    fun providesITunesService(@Named("itunes-retrofit") retrofit: Retrofit): ITunesService =
            retrofit.create(ITunesService::class.java)
}