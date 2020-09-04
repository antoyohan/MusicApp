package com.example.musicapp.ui.recycler

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.api.services.youtube.model.Playlist
import com.google.api.services.youtube.model.PlaylistItem
import com.google.api.services.youtube.model.PlaylistItemSnippet
import com.google.api.services.youtube.model.PlaylistSnippet
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.playlist_item.view.*

class PlaylistViewHolder<T>(view: View): RecyclerView.ViewHolder(view) {

    fun bind(playlist: T?, cellClickListener: CellClickListener<T>?) {
        playlist?.let {
            if( playlist is Playlist) {
                itemView.txt_news_name.text = playlist.snippet.title
                Picasso.get().load(playlist.snippet.thumbnails.default.url)
                    .into(itemView.img_news_banner)
            } else if (playlist is PlaylistItem) {
                itemView.txt_news_name.text = playlist.snippet.title
                Picasso.get().load(playlist.snippet.thumbnails.default.url)
                    .into(itemView.img_news_banner)
            }

            itemView.setOnClickListener {
                cellClickListener?.onCellClickListener(playlist)
            }
        }
    }
}