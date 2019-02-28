package es.lolrav.podsavior.di

import dagger.Module
import dagger.Provides
import es.lolrav.podsavior.data.MatchAndMerge
import es.lolrav.podsavior.database.entity.Series
import javax.inject.Inject

@Module
object TestMatchAndMergeModule {
    @[Provides JvmStatic]
    fun providesSeriesMatchAndMerge(merger: TestMerger): MatchAndMerge<Series> = merger

    class TestMerger
    @Inject
    constructor() : MatchAndMerge<Series> {
        override fun matches(left: Series, right: Series): Boolean = left.uid == right.uid
        override fun merge(bottom: Series, top: Series): Series = top
    }
}