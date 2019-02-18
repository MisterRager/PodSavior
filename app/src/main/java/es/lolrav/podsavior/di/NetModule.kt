package es.lolrav.podsavior.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.Reusable
import es.lolrav.podsavior.net.itunes.ITUNES_BASE_URI
import es.lolrav.podsavior.net.itunes.ITunesService
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named

@Module
object NetModule {
    @[Provides JvmStatic Reusable]
    fun providesMoshi(): Moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    @[Provides JvmStatic Reusable]
    @Named("itunes-retrofit")
    fun providesITunesRetrofit(moshi: Moshi): Retrofit =
            Retrofit.Builder()
                    .baseUrl(ITUNES_BASE_URI)
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()

    @[Provides JvmStatic Reusable]
    fun providesITunesService(@Named("itunes-retrofit") retrofit: Retrofit): ITunesService =
            retrofit.create(ITunesService::class.java)
}