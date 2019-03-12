package es.lolrav.podsavior.di

import dagger.Component
import es.lolrav.podsavior.data.time.TimeParsingModule
import es.lolrav.podsavior.data.xml.ParseFeedTest
import es.lolrav.podsavior.di.module.XmlParserModule

@Component(modules = [TimeParsingModule::class, XmlParserModule::class, TestSeriesModule::class])
interface FeedParserComponent {
    @Component.Builder
    interface Builder {
        fun build(): FeedParserComponent
    }

    fun inject(parseFeedTest: ParseFeedTest)
}