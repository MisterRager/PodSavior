package es.lolrav.podsavior.database.dao

import androidx.room.Dao
import androidx.room.Query
import es.lolrav.podsavior.database.entity.Episode
import io.reactivex.Flowable

@Dao
interface EpisodeDao : ItemInsertAndDelete<Episode> {
    @Query("SELECT * FROM ${Episode.TABLE_NAME}")
    fun getAll(): Flowable<List<Episode>>

    @Query("SELECT * FROM ${Episode.TABLE_NAME} WHERE seriesUid = :seriesUid")
    fun getBySeries(seriesUid: String): Flowable<List<Episode>>

    @Query("SELECT * FROM ${Episode.TABLE_NAME} WHERE uid = :uid LIMIT 1")
    fun getByUid(uid: String): Flowable<Episode>

    @Query("SELECT * FROM ${Episode.TABLE_NAME} WHERE uid IN(:uid)")
    fun getListByUid(vararg uid: String): Flowable<List<Episode>>
}