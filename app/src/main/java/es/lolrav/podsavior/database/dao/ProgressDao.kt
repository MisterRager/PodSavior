package es.lolrav.podsavior.database.dao

import androidx.room.Dao
import androidx.room.Query
import es.lolrav.podsavior.database.entity.Progress
import io.reactivex.Flowable

@Dao
interface ProgressDao : ItemInsertAndDelete<Progress> {
    @Query("SELECT * FROM ${Progress.TABLE_NAME} WHERE episodeUid = :episodeUid")
    fun getByEpisodeUid(episodeUid: String): Flowable<Progress>
}