package es.lolrav.podsavior.di

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import es.lolrav.podsavior.net.itunes.ITUNES_BASE_URI
import es.lolrav.podsavior.net.itunes.ITunesService
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named

@Module
class NetModule {
    @Provides
    fun providesMoshi(): Moshi = Moshi.Builder().build()

    @Provides
    @Named("itunes-retrofit")
    fun providesITunesRetrofit(moshi: Moshi): Retrofit =
            Retrofit.Builder()
                    .baseUrl(ITUNES_BASE_URI)
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .build()

    @Provides
    fun providesITunesService(@Named("itunes-retrofit") retrofit: Retrofit): ITunesService =
            retrofit.create(ITunesService::class.java)
}