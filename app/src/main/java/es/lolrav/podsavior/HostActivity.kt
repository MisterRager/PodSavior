package es.lolrav.podsavior

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import dagger.android.AndroidInjection

class HostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
    }
}
