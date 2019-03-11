package es.lolrav.podsavior.database.convert

import androidx.room.TypeConverter
import org.threeten.bp.Duration

object ThreeTenAbpConverter {
    @[TypeConverter JvmStatic]
    fun fromTimestamp(timeInMs: Long): Duration = Duration.ofMillis(timeInMs)

    @[TypeConverter JvmStatic]
    fun toTimestamp(duration: Duration): Long = duration.toMillis()
}