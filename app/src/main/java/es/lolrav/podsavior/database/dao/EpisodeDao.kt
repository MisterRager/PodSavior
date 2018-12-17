package es.lolrav.podsavior.database.dao

import androidx.room.Dao
import androidx.room.Query
import es.lolrav.podsavior.database.entity.Episode
import io.reactivex.Flowable

@Dao
interface EpisodeDao : ItemInsertAndDelete<Episode> {
    @Query("SELECT * FROM ${Episode.TABLE_NAME}")
    fun getAll(): Flowable<List<Episode>>
}