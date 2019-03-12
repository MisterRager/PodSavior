package es.lolrav.podsavior.data.time

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import org.threeten.bp.format.DateTimeFormatter

@Module
object TimeParsingModule {
    @[JvmStatic Provides IntoSet]
    fun providesRfc1123Formatter(): Pair<Regex, DateTimeFormatter> =
            Regex("[A-Za-z]+, [0-9]+ [A-Za-z]+ [0-9]+ [0-9]+:[0-9]+:[0-9]+ [+-]?[0-9]+") to
                    DateTimeFormatter.RFC_1123_DATE_TIME

    @[JvmStatic Provides IntoSet]
    fun providesAlmostRfc1123Formatter(): Pair<Regex, DateTimeFormatter> =
            Regex("[A-Za-z]+, [0-9]+ [A-Za-z]+ [0-9]+ [0-9]+:[0-9]+:[0-9]+ [A-Z_a-z]+") to
                    DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss zzz")

    @[JvmStatic Provides FallbackFormatter]
    fun providesFallbackFormatter(): DateTimeFormatter = DateTimeFormatter.RFC_1123_DATE_TIME
}