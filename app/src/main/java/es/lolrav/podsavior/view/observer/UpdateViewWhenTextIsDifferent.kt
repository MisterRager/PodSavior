package es.lolrav.podsavior.view.observer

import android.widget.TextView
import androidx.lifecycle.Observer

class UpdateViewWhenTextIsDifferent<V: TextView, S: CharSequence>(private val view: V) : Observer<S> {
    override fun onChanged(t: S) {
        if (view.text.toString() != t.toString()) {
            view.text = t
        }
    }
}