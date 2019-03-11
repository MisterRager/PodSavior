package es.lolrav.podsavior.di.module

import dagger.Module
import dagger.Provides
import dagger.Reusable
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory

@Module
object XmlParserModule {
    @[JvmStatic Reusable Provides]
    fun providesXmlParserFactory(): XmlPullParserFactory =
            XmlPullParserFactory.newInstance()
                    .apply {
                        isNamespaceAware = true
                    }

    @[JvmStatic Reusable Provides]
    fun providesXmlParser(factory: XmlPullParserFactory): XmlPullParser = factory.newPullParser()
}