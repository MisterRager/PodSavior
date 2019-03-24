package es.lolrav.podsavior.data.merge

import dagger.Reusable
import es.lolrav.podsavior.data.MatchAndMerge
import es.lolrav.podsavior.database.entity.Episode
import javax.inject.Inject

@Reusable
class MergeEpisode
@Inject
constructor() : MatchAndMerge<Episode> {
    override fun matches(left: Episode, right: Episode): Boolean = left.uid == right.uid
    override fun merge(bottom: Episode, top: Episode): Episode =
            Episode(
                    uid = bottom.uid,
                    seriesUid = bottom.seriesUid,
                    name = top.name,
                    description = top.description ?: bottom.description,
                    descriptionMarkup = top.descriptionMarkup ?: bottom.descriptionMarkup,
                    episodeUri = top.episodeUri,
                    onDiskPath = if (top.onDiskPath.isNullOrBlank()) {
                        bottom.onDiskPath
                    } else {
                        top.onDiskPath
                    },
                    imageUri = top.imageUri ?: bottom.imageUri,
                    duration = top.duration,
                    publishTime = top.publishTime)
}