package es.lolrav.podsavior.view.series.view

import android.text.Html
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import dagger.Reusable
import es.lolrav.podsavior.database.entity.Episode
import es.lolrav.podsavior.view.common.BasicViewHolder
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import javax.inject.Inject
import kotlin.concurrent.withLock

@Reusable
class EpisodeAdapter
@Inject
constructor(
        private val picasso: Picasso
) : RecyclerView.Adapter<BasicViewHolder<EpisodeRow>>() {
    private val episodesLock: Lock by lazy { ReentrantLock() }
    private val episodes: MutableList<Episode> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasicViewHolder<EpisodeRow> =
            BasicViewHolder(EpisodeRow(parent.context).apply {
                layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            })

    override fun getItemCount(): Int = episodes.size

    override fun onBindViewHolder(holder: BasicViewHolder<EpisodeRow>, position: Int) {
        episodesLock.withLock {
            episodes.getOrNull(position)?.also { episode ->
                episode.imageUri
                        ?.takeIf(String::isNotBlank)
                        ?.let(picasso::load)
                        ?.into(holder.heldView.episodeIcon)
                holder.heldView.title.text = episode.name
                holder.heldView.summary.text =
                        episode.descriptionMarkup?.let(Html::fromHtml) ?: episode.description ?: ""
                holder.heldView.duration.text = episode.duration.toString()
            }
        }
    }

    fun setEpisodes(episodes: List<Episode>) {
        episodesLock.withLock {
            this.episodes.apply {
                clear()
                addAll(episodes)
            }
        }
    }
}