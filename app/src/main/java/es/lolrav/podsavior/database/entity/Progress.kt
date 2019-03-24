package es.lolrav.podsavior.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import es.lolrav.podsavior.database.convert.ThreeTenAbpConverter
import es.lolrav.podsavior.database.entity.Progress.Companion.TABLE_NAME
import org.threeten.bp.Duration
import org.threeten.bp.Instant

@TypeConverters(ThreeTenAbpConverter::class)
@Entity(tableName = TABLE_NAME)
data class Progress(
        @PrimaryKey
        val episodeUid: String,
        val duration: Duration,
        val timestamp: Instant
) {
        companion object {
                const val TABLE_NAME = "progress"
        }
}