package es.lolrav.podsavior.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Series(
        @PrimaryKey
        val uid: String,
        val name: String
) {
        companion object {
                const val TABLE_NAME = "series"
        }
}
