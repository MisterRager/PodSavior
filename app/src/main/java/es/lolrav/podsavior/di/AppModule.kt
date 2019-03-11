package es.lolrav.podsavior.di

import android.app.Application
import android.content.Context
import androidx.work.WorkManager
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import dagger.Reusable
import es.lolrav.podsavior.di.qualifiers.DataScheduler
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import javax.inject.Named

const val APP_CONTEXT = "application"
@Module
object AppModule {
    @Named(APP_CONTEXT)
    @[JvmStatic Reusable Provides]
    fun providesContext(app: Application): Context = app

    @[JvmStatic Provides DataScheduler Reusable]
    fun providesDataScheduler(): Scheduler = Schedulers.io()

    @[JvmStatic Reusable Provides]
    fun providesWorkManager(): WorkManager = WorkManager.getInstance()

    @[JvmStatic Reusable Provides]
    fun providesPicasso(): Picasso = Picasso.get()

    @[JvmStatic Reusable Provides]
    fun providesXmlParserFactory(): XmlPullParserFactory =
            XmlPullParserFactory.newInstance()
                    .apply {
                        isNamespaceAware = true
                    }

    @[JvmStatic Reusable Provides]
    fun providesXmlParser(factory: XmlPullParserFactory): XmlPullParser = factory.newPullParser()
}