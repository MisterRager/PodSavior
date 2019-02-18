package es.lolrav.podsavior

import androidx.multidex.MultiDexApplication
import es.lolrav.podsavior.di.AppComponent
import es.lolrav.podsavior.di.DaggerAppComponent
import es.lolrav.podsavior.di.has.HasAppComponent

class PodApplication: MultiDexApplication(), HasAppComponent {
    override val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
                .application(this)
                .build()
    }
}
