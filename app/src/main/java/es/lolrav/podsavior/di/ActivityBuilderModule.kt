package es.lolrav.podsavior.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import es.lolrav.podsavior.HostActivity

@Module
abstract class ActivityBuilderModule {
    @ContributesAndroidInjector
    abstract fun bindHostActivity(): HostActivity
}