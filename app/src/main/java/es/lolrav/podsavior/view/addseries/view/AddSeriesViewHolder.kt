package es.lolrav.podsavior.view.addseries.view

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import es.lolrav.podsavior.R

class AddSeriesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val title: TextView by lazy { view.findViewById<TextView>(R.id.add_series_title) }
    val artist: TextView by lazy { view.findViewById<TextView>(R.id.add_series_artist) }
    val icon: ImageView by lazy { view.findViewById<ImageView>(R.id.add_series_icon) }
}