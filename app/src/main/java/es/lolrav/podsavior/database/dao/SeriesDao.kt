package es.lolrav.podsavior.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import es.lolrav.podsavior.database.entity.Series
import io.reactivex.Flowable

@Dao
interface SeriesDao : ItemInsertAndDelete<Series> {
    @Query("SELECT * FROM ${Series.TABLE_NAME}")
    fun getAll(): Flowable<List<Series>>

    @Query("SELECT * FROM ${Series.TABLE_NAME} WHERE name LIKE :query || '%'")
    fun findByName(query: String): Flowable<List<Series>>

    @Query("SELECT * FROM ${Series.TABLE_NAME} WHERE uid = :uid")
    fun findByUid(uid: String): Flowable<Series>

    @Query("SELECT * FROM ${Series.TABLE_NAME} WHERE isSubscribed")
    fun findAllSubscriptions(): Flowable<List<Series>>
}