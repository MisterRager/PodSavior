package es.lolrav.podsavior.view.series.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import es.lolrav.podsavior.R

class EpisodeRow : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, theme: Int) : super(context, attrs, theme)

    init {
        inflate(context, R.layout.episode_row, this)
    }

    val episodeIcon: ImageView by lazy { findViewById<ImageView>(R.id.episode_icon) }
    val title: TextView by lazy { findViewById<TextView>(R.id.episode_title) }
    val summary: TextView by lazy { findViewById<TextView>(R.id.episode_summary) }
    val duration: TextView by lazy { findViewById<TextView>(R.id.episode_duration) }
}