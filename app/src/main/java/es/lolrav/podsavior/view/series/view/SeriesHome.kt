package es.lolrav.podsavior.view.series.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import es.lolrav.podsavior.R

/**
 * TODO: document your custom view class.
 */
class SeriesHome : ConstraintLayout {
    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        inflate(context, R.layout.series_detail, this)
    }

    val coverImage: ImageView by lazy { findViewById<ImageView>(R.id.series_cover_image) }
    val title: TextView by lazy { findViewById<TextView>(R.id.series_title) }
    val descriptionView: TextView by lazy { findViewById<TextView>(R.id.series_description) }
    val episodesView: RecyclerView by lazy { findViewById<RecyclerView>(R.id.episodes) }
}
