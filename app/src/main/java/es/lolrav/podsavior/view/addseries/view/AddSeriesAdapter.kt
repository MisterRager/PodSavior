package es.lolrav.podsavior.view.addseries.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import es.lolrav.podsavior.R
import es.lolrav.podsavior.database.entity.Series
import io.reactivex.processors.PublishProcessor
import org.reactivestreams.Processor
import org.reactivestreams.Publisher
import javax.inject.Inject

class AddSeriesAdapter
@Inject
constructor() : RecyclerView.Adapter<AddSeriesViewHolder>() {
    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddSeriesViewHolder =
            AddSeriesViewHolder(
                    LayoutInflater.from(parent.context)
                            .inflate(R.layout.add_series_row, parent, false)
            ).apply { itemView.setOnClickListener { onClicks.onNext(items[adapterPosition]) } }

    override fun onBindViewHolder(holder: AddSeriesViewHolder, position: Int) {
        items[position].let { series ->
            holder.title.text = series.name
            series.artistName?.let { holder.artist.text = it }
            series.iconPath
                    ?.takeIf { it.isNotBlank() }
                    ?.let { iconPath -> Picasso.get().load(iconPath) }
                    ?.into(holder.icon)

            holder.statusCheck.isChecked = series.isSubscribed
        }
    }

    var items: MutableList<Series> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val onClicks: Processor<Series, Series> = PublishProcessor.create()
    val onClick: Publisher<Series> = onClicks
}