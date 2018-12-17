package es.lolrav.podsavior.view.addepisode.viewmodel

import androidx.lifecycle.ViewModel
import es.lolrav.podsavior.database.entity.Series
import es.lolrav.podsavior.data.ItemSource
import javax.inject.Inject

class AddSeriesViewModel
@Inject
constructor(
        val seriesSources: Set<ItemSource<Series>>
) : ViewModel() {
}