package es.lolrav.podsavior.data.time

import dagger.Reusable
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.TemporalQuery
import javax.inject.Inject
import javax.inject.Qualifier

@Reusable
class PatternMatchingParser
@Inject
constructor(
        private val formatterSet: Set<Pair<Regex, DateTimeFormatter>>,
        @FallbackFormatter private val fallbackFormatter: DateTimeFormatter
) {
    fun <T> parse(time: String, toFormat: TemporalQuery<T>): T =
            (formatterSet.find { (regex, _) -> regex.matches(time) }
                    ?.second
                    ?.parse(time, toFormat)
                    ?: fallbackFormatter.parse(time, toFormat))
}

@Qualifier
annotation class FallbackFormatter