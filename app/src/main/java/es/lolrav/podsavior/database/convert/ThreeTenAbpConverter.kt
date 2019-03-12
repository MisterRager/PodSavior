package es.lolrav.podsavior.database.convert

import androidx.room.TypeConverter
import org.threeten.bp.Duration
import org.threeten.bp.Instant

object ThreeTenAbpConverter {
    @[TypeConverter JvmStatic]
    fun durationFromTimestamp(timeInMs: Long): Duration = Duration.ofMillis(timeInMs)

    @[TypeConverter JvmStatic]
    fun toTimestamp(duration: Duration): Long = duration.toMillis()

    @[TypeConverter JvmStatic]
    fun instantFromTimestamp(epochTimeInMs: Long): Instant = Instant.ofEpochMilli(epochTimeInMs)

    @[TypeConverter JvmStatic]
    fun toTimestamp(time: Instant): Long = time.toEpochMilli()
}