package es.lolrav.podsavior.di

import android.content.Context
import dagger.Module
import es.lolrav.podsavior.PodApplication

@Module
interface AppModule {
    fun providesContext(app: PodApplication): Context = app
}