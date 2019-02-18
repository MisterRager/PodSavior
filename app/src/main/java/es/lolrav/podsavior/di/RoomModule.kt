package es.lolrav.podsavior.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.Reusable
import es.lolrav.podsavior.database.PodDatabase
import es.lolrav.podsavior.database.dao.EpisodeDao
import es.lolrav.podsavior.database.dao.SeriesDao
import javax.inject.Named
import javax.inject.Singleton

@Module
object RoomModule {
    @Named("database-name")
    @[Provides JvmStatic Reusable]
    fun providesDatabaseName(): String = "pods-and-ends"

    @[Provides JvmStatic Singleton]
    fun providePodDatabase(
            @Named(APP_CONTEXT) context: Context,
            @Named("database-name") dbName: String
    ): PodDatabase = Room.databaseBuilder(context, PodDatabase::class.java, dbName).build()

    @[Provides JvmStatic Reusable]
    fun provideEpisodeDao(podDatabase: PodDatabase): EpisodeDao = podDatabase.episodeDao

    @[Provides JvmStatic Reusable]
    fun provideSeriesDao(podDatabase: PodDatabase): SeriesDao = podDatabase.seriesDao
}