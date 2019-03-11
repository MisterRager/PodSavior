package es.lolrav.podsavior.di

import dagger.Component
import es.lolrav.podsavior.data.merge.MergeSeriesTest

@Component(modules = [TestSeriesModule::class])
interface MergeSeriesTestComponent {
    @Component.Builder
    interface Buidler {
        fun build(): MergeSeriesTestComponent
    }

    fun inject(test: MergeSeriesTest)
}