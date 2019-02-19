package es.lolrav.podsavior.view.home.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import dagger.Reusable
import es.lolrav.podsavior.R
import es.lolrav.podsavior.database.entity.Series
import javax.inject.Inject

@Reusable
class SubscriptionAdapter
@Inject
constructor() : RecyclerView.Adapter<SubscriptionAdapter.ViewHolder>() {
    var items: List<Series> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(
                    LayoutInflater.from(parent.context)
                            .inflate(R.layout.subscription_row, parent, false))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        items[position].also { item ->
            holder.title.text = item.name
            item.artistName?.let { artistName -> holder.artist.text = artistName }

            item.iconPath
                    ?.let { iconUri -> Picasso.get().load(iconUri) }
                    ?.into(holder.icon)
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView by lazy { view.findViewById<ImageView>(R.id.subscription_icon) }
        val title: TextView by lazy { view.findViewById<TextView>(R.id.subscription_title) }
        val artist: TextView by lazy { view.findViewById<TextView>(R.id.subscription_artist) }
        val latestDate: TextView by lazy { view.findViewById<TextView>(R.id.subscription_latest_date) }
    }
}