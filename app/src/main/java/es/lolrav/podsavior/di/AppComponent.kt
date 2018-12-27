package es.lolrav.podsavior.di

import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import es.lolrav.podsavior.PodApplication

@Component(modules = [
    //ActivityBuilderModule::class,
    AndroidInjectionModule::class,
    RoomModule::class,
    AppModule::class
])
interface AppComponent : AndroidInjector<PodApplication> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<PodApplication>()
}