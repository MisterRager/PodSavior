package es.lolrav.podsavior.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import es.lolrav.podsavior.database.PodDatabase
import es.lolrav.podsavior.database.dao.EpisodeDao
import es.lolrav.podsavior.database.dao.SeriesDao
import javax.inject.Named
import javax.inject.Singleton

@Module
class RoomModule {
    @Provides @Named("database-name")
    fun providesDatabaseName(): String = "pods-and-ends"

    @[Provides Singleton]
    fun providePodDatabase(
            context: Context,
            @Named("database-name") dbName: String
    ): PodDatabase = Room.databaseBuilder(context, PodDatabase::class.java, dbName).build()

    @Provides
    fun provideEpisodeDao(podDatabase: PodDatabase): EpisodeDao = podDatabase.episodeDao

    @Provides
    fun provideSeriesDao(podDatabase: PodDatabase): SeriesDao = podDatabase.seriesDao
}