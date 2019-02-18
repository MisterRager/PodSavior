package es.lolrav.podsavior.di.has

import android.content.Context
import es.lolrav.podsavior.di.AppComponent

interface HasAppComponent {
    val appComponent: AppComponent
}

val Context.appComponent: AppComponent?
    get() =
        ((this as? HasAppComponent) ?: (applicationContext as? HasAppComponent))?.appComponent

