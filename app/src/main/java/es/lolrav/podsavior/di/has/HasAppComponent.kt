package es.lolrav.podsavior.di.has

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import es.lolrav.podsavior.di.AppComponent

interface HasAppComponent {
    val appComponent: AppComponent
}

val Context.appComponent: AppComponent?
    get() =
        (this as? HasAppComponent)?.appComponent ?:
        (this as? ContextWrapper)?.baseContext?.appComponent ?:
        (applicationContext as? HasAppComponent)?.appComponent

val Application.appComponent: AppComponent get() = (this as HasAppComponent).appComponent
