package es.lolrav.podsavior.data.merge

import es.lolrav.podsavior.data.MatchAndMerge
import es.lolrav.podsavior.database.entity.Series
import javax.inject.Inject

class MergeSeries
@Inject
constructor() : MatchAndMerge<Series> {
    // TODO: use something other than the generated UID - doesn't work across sources
    override fun matches(left: Series, right: Series): Boolean = left.uid == right.uid

    override fun merge(bottom: Series, top: Series): Series =
            Series(
                    uid = bottom.uid,
                    name = top.name,
                    artistName = top.artistName,
                    feedUri = top.feedUri,
                    description = top.description ?: bottom.description,
                    isSubscribed = (top.isSaved && top.isSubscribed) ||
                            (!top.isSaved &&
                                    bottom.isSaved &&
                                    bottom.isSubscribed),
                    isSaved = bottom.isSaved || top.isSaved,
                    iconPath = top.iconPath ?: bottom.iconPath)
}