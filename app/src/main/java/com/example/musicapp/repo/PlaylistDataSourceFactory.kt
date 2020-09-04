package com.example.musicapp.repo

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource.Factory
import com.google.api.services.youtube.model.Playlist
import io.reactivex.disposables.CompositeDisposable

class PlaylistDataSourceFactory(val compositeDisposable: CompositeDisposable) :
    Factory<String, Playlist>() {

    val playlistDataSourceLive = MutableLiveData<PlaylistDataSource>()
    override fun create(): androidx.paging.DataSource<String, Playlist> {
        val dataSource = PlaylistDataSource(compositeDisposable)
        playlistDataSourceLive.postValue(dataSource)
        return dataSource
    }


}