package com.example.musicapp.ui.recycler

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R
import com.google.api.services.youtube.model.Playlist
import com.google.api.services.youtube.model.PlaylistItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.playlist_item.view.*

class PlaylistViewHolder<T>(view: View): RecyclerView.ViewHolder(view) {

    fun bind(playlist: T?, cellClickListener: CellClickListener<T>?) {
        playlist?.let {
            if( playlist is Playlist) {
                itemView.title.text = playlist.snippet.title
                itemView.subtitle2.text = getVideoCount(itemView, playlist.contentDetails.itemCount)
                Picasso.get().load(playlist.snippet.thumbnails.high.url)
                    .into(itemView.img_news_banner)
            } else if (playlist is PlaylistItem) {
                itemView.title.text = playlist.snippet.title
                itemView.subtitle1.text = playlist.snippet.channelTitle
                Picasso.get().load(playlist.snippet.thumbnails.high.url)
                    .into(itemView.img_news_banner)
            }

            itemView.setOnClickListener {
                cellClickListener?.onCellClickListener(playlist)
            }
        }
    }

    private fun getVideoCount(itemView: View, itemCount: Long): CharSequence? {
        return itemView.context.getString(if(itemCount > 1) R.string.songs_numbers else R.string.songs_number, itemCount)
    }
}