package es.lolrav.podsavior.database

import androidx.room.Database
import androidx.room.RoomDatabase
import es.lolrav.podsavior.database.dao.EpisodeDao
import es.lolrav.podsavior.database.dao.ProgressDao
import es.lolrav.podsavior.database.dao.SeriesDao
import es.lolrav.podsavior.database.entity.Episode
import es.lolrav.podsavior.database.entity.Progress
import es.lolrav.podsavior.database.entity.Series

@Database(entities = [Series::class, Episode::class, Progress::class], version = 1)
abstract class PodDatabase : RoomDatabase() {
    abstract val seriesDao: SeriesDao
    abstract val episodeDao: EpisodeDao
    abstract val progressDao: ProgressDao
}