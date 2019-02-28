package es.lolrav.podsavior.di

import dagger.Module
import dagger.Provides
import es.lolrav.podsavior.database.entity.Series
import java.util.*
import javax.inject.Qualifier

@Module
object TestSeriesModule {
    @[Provides JvmStatic SeriesRandom]
    fun providesSeriesRandom(): Random = Random()

    @[Provides JvmStatic]
    fun providesSeries(@SeriesRandom random: Random): Series =
            Series(
                    uid = randomUuid,
                    name = randomUuid,
                    artistName = randomUuid,
                    feedUri = randomUuid,
                    description = randomUuid,
                    isSubscribed = random.nextBoolean(),
                    isSaved = random.nextBoolean(),
                    iconPath = randomUuid)

    private val randomUuid: String = UUID.randomUUID().toString()
}

@Qualifier
annotation class SeriesRandom
