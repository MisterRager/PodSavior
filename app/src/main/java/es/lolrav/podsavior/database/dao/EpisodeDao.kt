package es.lolrav.podsavior.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import es.lolrav.podsavior.database.entity.Episode
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface EpisodeDao : ItemInsertAndDelete<Episode> {
    @Query("SELECT * FROM ${Episode.TABLE_NAME}")
    fun getAll(): Flowable<List<Episode>>

    @Query("SELECT * FROM ${Episode.TABLE_NAME} WHERE seriesUid = :seriesUid")
    fun getBySeries(seriesUid: String): Flowable<List<Episode>>

    @Query("SELECT * FROM ${Episode.TABLE_NAME} WHERE uid = :uid LIMIT 1")
    fun getByUid(uid: String): Flowable<Episode>

    //@Query("UPDATE ${Episode.TABLE_NAME} SET onDiskPath = :path WHERE uid = :uid AND onDiskPath IS NULL")
    @Query("""
        UPDATE ${Episode.TABLE_NAME}
        SET onDiskPath = :path WHERE uid = :uid AND onDiskPath IS NULL
    """)
    fun updateFilePathIfBlank(uid: String, path: String): Single<Int>

    @Query("SELECT * FROM ${Episode.TABLE_NAME} WHERE uid IN(:uid)")
    fun getListByUid(vararg uid: String): Flowable<List<Episode>>
}