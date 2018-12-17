package es.lolrav.podsavior.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import es.lolrav.podsavior.database.entity.Episode.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class Episode(
        @PrimaryKey
        val uid: String,
        val seriesUid: String,
        val name: String
) {
        companion object {
            const val TABLE_NAME = "episode"
        }
}
