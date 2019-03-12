package es.lolrav.podsavior.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import es.lolrav.podsavior.database.convert.ThreeTenAbpConverter
import es.lolrav.podsavior.database.entity.Episode.Companion.TABLE_NAME
import org.threeten.bp.Duration
import org.threeten.bp.Instant

@TypeConverters(ThreeTenAbpConverter::class)
@Entity(tableName = TABLE_NAME)
data class Episode(
        @PrimaryKey
        val uid: String,
        val seriesUid: String,
        val name: String,
        val description: String?,
        val descriptionMarkup: String?,
        val episodeUri: String,
        val imageUri: String?,
        val duration: Duration,
        val publishTime: Instant
) {
        companion object {
            const val TABLE_NAME = "episode"
        }
}
