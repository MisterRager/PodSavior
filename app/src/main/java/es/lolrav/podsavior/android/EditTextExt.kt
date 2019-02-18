package es.lolrav.podsavior.android

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import io.reactivex.Observable
import io.reactivex.ObservableEmitter

val EditText.onTextIsUpdatedEditables: Observable<MaybeEditable>
    get() =
        Observable.using(
                ::AfterTextChangedRxWatcher,
                { listener: AfterTextChangedRxWatcher ->
                    addTextChangedListener(listener)
                    Observable.create { emitter: ObservableEmitter<MaybeEditable> ->
                        listener.emitter = emitter
                    }
                },
                this::removeTextChangedListener
        )

val EditText.onTextIsUpdated: Observable<CharSequence>
    get() = onTextIsUpdatedEditables.map { it.editable ?: "" }

/**
 * This class does not buffer or cache events - if you're not subscribed, you miss them.
 */
class AfterTextChangedRxWatcher : TextWatcher {
    var emitter: ObservableEmitter<MaybeEditable>? = null

    override fun afterTextChanged(s: Editable?) {
        emitter?.onNext(MaybeEditable(s))
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
}

data class MaybeEditable(val editable: Editable?) {
    val asString: String? get() = editable?.toString()
}
