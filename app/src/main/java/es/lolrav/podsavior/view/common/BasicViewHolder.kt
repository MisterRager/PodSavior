package es.lolrav.podsavior.view.common

import android.view.View
import androidx.recyclerview.widget.RecyclerView

class BasicViewHolder<T: View>(val heldView: T) : RecyclerView.ViewHolder(heldView)