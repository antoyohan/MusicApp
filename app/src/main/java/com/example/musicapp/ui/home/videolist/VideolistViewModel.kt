package com.example.musicapp.ui.home.videolist

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.musicapp.model.PlaylistData
import com.example.musicapp.repo.videolist.PlayLisItemDataSource
import com.example.musicapp.repo.videolist.PlaylistItemDataSourceFactory
import com.example.musicapp.utils.State
import com.google.api.services.youtube.model.PlaylistItem
import io.reactivex.disposables.CompositeDisposable

class VideolistViewModel(val playlistdata:PlaylistData): ViewModel() {
    private val compositeDisposable: CompositeDisposable
    private val playlistItemDataSourceFactory: PlaylistItemDataSourceFactory

    var playlists: LiveData<PagedList<PlaylistItem>>
    val pageSize = 5

    init {
        compositeDisposable = CompositeDisposable()
        playlistItemDataSourceFactory = PlaylistItemDataSourceFactory(compositeDisposable, playlistdata.id?:"")
        val config = PagedList.Config.Builder()
            .setInitialLoadSizeHint(pageSize)
            .setPageSize(pageSize * 2)
            .setEnablePlaceholders(false)
            .build()
        playlists = LivePagedListBuilder<String, PlaylistItem>(playlistItemDataSourceFactory, config).build()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun getState(): LiveData<State> = Transformations.switchMap<PlayLisItemDataSource,
            State>(playlistItemDataSourceFactory.playlistDataSourceLive, PlayLisItemDataSource::state)

    fun retry() {
        //playlistDataSourceFactory.playlistDataSourceLive.value?.retry()
    }

    fun listIsEmpty(): Boolean {
        return playlists.value?.isEmpty() ?: true
    }
}