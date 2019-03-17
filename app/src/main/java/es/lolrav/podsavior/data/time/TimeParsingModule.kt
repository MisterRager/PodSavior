package es.lolrav.podsavior.data.time

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Qualifier

@Module
object TimeParsingModule {
    @[JvmStatic Provides Offset]
    fun providesRfc1123Formatter(): DateTimeFormatter = DateTimeFormatter.RFC_1123_DATE_TIME

    @[JvmStatic Provides IntoSet]
    fun providesRfc1123FormatterMap(
            @Offset formatter: DateTimeFormatter
    ): Pair<Regex, DateTimeFormatter> =
            Regex("[A-Za-z]+, [0-9]+ [A-Za-z]+ [0-9]+ [0-9]+:[0-9]+:[0-9]+ [+-]?[0-9]+") to
                    formatter

    @[JvmStatic Provides Zoned]
    fun providesPodCastFormatter(): DateTimeFormatter =
            DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss zzz")

    @[JvmStatic Provides IntoSet]
    fun providesAlmostRfc1123Formatter(
            @Zoned formatter: DateTimeFormatter
    ): Pair<Regex, DateTimeFormatter> =
            Regex("[A-Za-z]+, [0-9]+ [A-Za-z]+ [0-9]+ [0-9]+:[0-9]+:[0-9]+ [A-Z_a-z]+") to
                    formatter

    @[JvmStatic Provides FallbackFormatter]
    fun providesFallbackFormatter(
            @Zoned formatter: DateTimeFormatter
    ): DateTimeFormatter = formatter
}

@Qualifier
annotation class Zoned

@Qualifier
annotation class Offset