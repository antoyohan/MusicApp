package com.example.musicapp.ui.list

import android.view.View
import android.view.View.VISIBLE
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.utils.State
import kotlinx.android.synthetic.main.item_list_footer.view.*

class ListFooterViewHolder(view:View): RecyclerView.ViewHolder(view) {

    fun bind(status: State?) {
        itemView.progress_bar.visibility = if (status == State.LOADING) VISIBLE else View.INVISIBLE
        itemView.txt_error.visibility = if (status == State.ERROR) VISIBLE else View.INVISIBLE
    }
}