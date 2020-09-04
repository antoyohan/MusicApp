package com.example.musicapp.repo.videolist

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.google.api.services.youtube.model.PlaylistItem
import io.reactivex.disposables.CompositeDisposable

class PlaylistItemDataSourceFactory(val compositeDisposable: CompositeDisposable, val id: String) :
    DataSource.Factory<String, PlaylistItem>() {

    val playlistDataSourceLive = MutableLiveData<PlayLisItemDataSource>()
    override fun create(): androidx.paging.DataSource<String, PlaylistItem> {
        val dataSource = PlayLisItemDataSource(compositeDisposable, id)
        playlistDataSourceLive.postValue(dataSource)
        return dataSource
    }


}