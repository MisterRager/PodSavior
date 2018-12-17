package es.lolrav.podsavior.database.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import io.reactivex.Completable

interface ItemInsertAndDelete<T> {
    @Insert(onConflict = REPLACE)
    fun save(vararg items: T): Completable

    @Delete
    fun delete(vararg items: T): Completable
}