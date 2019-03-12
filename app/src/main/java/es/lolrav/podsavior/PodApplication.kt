package es.lolrav.podsavior

import androidx.multidex.MultiDexApplication
import com.jakewharton.threetenabp.AndroidThreeTen
import es.lolrav.podsavior.di.AppComponent
import es.lolrav.podsavior.di.DaggerAppComponent
import es.lolrav.podsavior.di.has.HasAppComponent

class PodApplication: MultiDexApplication(), HasAppComponent {
    override val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
                .application(this)
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this);
    }
}
