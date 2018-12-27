package es.lolrav.podsavior.view.addseries.view

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import es.lolrav.podsavior.R

class AddSeriesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val title: TextView by lazy { view.findViewById<TextView>(R.id.add_series_title) }
}