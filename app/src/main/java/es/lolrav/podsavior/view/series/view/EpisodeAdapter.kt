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
import org.threeten.bp.Duration
import org.threeten.bp.Instant
import org.threeten.bp.MonthDay
import org.threeten.bp.ZoneId
import org.threeten.bp.format.TextStyle
import java.util.*
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
                holder.heldView.apply {
                    episode.imageUri
                            ?.takeIf(String::isNotBlank)
                            ?.let(picasso::load)
                            ?.into(episodeIcon)

                    title.text = episode.name
                    summary.text =
                            episode.descriptionMarkup?.let(Html::fromHtml) ?: episode.description
                                    ?: ""
                    duration.text = episode.duration.formatted
                    date.text = episode.publishTime.formatted

                    subscribe.isChecked = episode.onDiskPath != null
                }
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

    override fun getItemId(position: Int): Long = episodes[position].uid.hashCode().toLong()

    private val Instant.formatted: String
        get() = MonthDay.from(atZone(ZoneId.systemDefault()))
                .let { date ->
                    date.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                            .let { monthName -> "$monthName ${date.dayOfMonth}" }
                }

    private val Duration.formatted: String
        get() =
            if (toDays() == 0L) {
                if (toHours() == 0L) {
                    if (toMinutes() == 0L) {
                        secondStr
                    } else {
                        "$minuteStr:$secondStr"
                    }
                } else {
                    "$hourStr:$minuteStr:$secondStr"
                }
            } else {
                "${toDays()} days"
            }

    private val Duration.secondStr: String get() = String.format("%02d", seconds % 60)
    private val Duration.minuteStr: String get() = String.format("%02d", toMinutes() % 60)
    private val Duration.hourStr: String get() = toHours().toString()
}