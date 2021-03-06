package com.example.musicapp.ui.recycler

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R
import com.example.musicapp.utils.State
import com.google.api.services.youtube.model.Playlist
import com.google.api.services.youtube.model.PlaylistSnippet

private const val TAG = "PlaylistRecyclerAdapter"
class PlaylistRecyclerAdapter: PagedListAdapter<Playlist, RecyclerView.ViewHolder>(DiffCallback) {

    private val DATA_VIEW_TYPE = 1
    private val FOOTER_VIEW_TYPE = 2

    private var state = State.LOADING
    var onCellClickListener:CellClickListener<Playlist>? = null
        set(value) {field = value}

    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<Playlist>() {
            override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
                Log.d(TAG, "areItemsTheSame: ${oldItem.id == newItem.id}")
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
                Log.d(TAG, "areContentsTheSame: ${oldItem == newItem}")
                return oldItem == newItem
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == DATA_VIEW_TYPE) {
           return PlaylistViewHolder<PlaylistSnippet>(LayoutInflater.from(parent.context)
               .inflate(R.layout.playlist_item, parent, false))
        } else {
            return ListFooterViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_footer, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == DATA_VIEW_TYPE)
            (holder as PlaylistViewHolder<Playlist>).bind(getItem(position), onCellClickListener)
        else (holder as ListFooterViewHolder).bind(state)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < super.getItemCount()) DATA_VIEW_TYPE else FOOTER_VIEW_TYPE
    }


    override fun getItemCount(): Int {
        return super.getItemCount()+ if (hasFooter()) 1 else 0
    }

    private fun hasFooter(): Boolean {
        return super.getItemCount() != 0 && (state == State.LOADING || state == State.ERROR)
    }

    fun setState(state: State) {
        this.state = state
        notifyItemChanged(super.getItemCount())
    }


}

interface CellClickListener<T> {
    fun onCellClickListener(data: T)
}