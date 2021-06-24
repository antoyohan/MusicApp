package com.example.musicapp.ui.list

import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.api.services.youtube.model.Playlist
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.playlist_item.view.*

class PlaylistViewHolder(view: View): RecyclerView.ViewHolder(view) {

    fun bind(playlist: Playlist?) {
        playlist?.let {
            itemView.txt_news_name.text = playlist.snippet.title
            Picasso.get().load(playlist.snippet.thumbnails.default.url).into(itemView.img_news_banner)
        }
    }
}