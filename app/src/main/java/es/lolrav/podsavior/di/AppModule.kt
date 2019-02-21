package es.lolrav.podsavior.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.Reusable
import es.lolrav.podsavior.di.qualifiers.DataScheduler
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import javax.inject.Named

const val APP_CONTEXT = "application"
@Module
object AppModule {
    @Named(APP_CONTEXT)
    @[JvmStatic Reusable Provides]
    fun providesContext(app: Application): Context = app

    @[JvmStatic Provides DataScheduler Reusable]
    fun providesDataScheduler(): Scheduler = Schedulers.io()
}