package com.example.musicapp.ui.home.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.musicapp.repo.playlist.PlaylistDataSource
import com.example.musicapp.repo.playlist.PlaylistDataSourceFactory
import com.example.musicapp.utils.Status
import com.google.api.services.youtube.model.Playlist
import io.reactivex.disposables.CompositeDisposable

class PlaylistViewModel : ViewModel() {
    private val compositeDisposable: CompositeDisposable
    private val playlistDataSourceFactory: PlaylistDataSourceFactory
    var playlists: LiveData<PagedList<Playlist>>
    val pageSize = 5

    init {
        compositeDisposable = CompositeDisposable()
        playlistDataSourceFactory =
            PlaylistDataSourceFactory(
                compositeDisposable
            )
        val config = PagedList.Config.Builder()
            .setInitialLoadSizeHint(pageSize)
            .setPageSize(pageSize * 2)
            .setEnablePlaceholders(false)
            .build()
        playlists =
            LivePagedListBuilder<String, Playlist>(playlistDataSourceFactory, config).build()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun getState(): LiveData<Status> = Transformations.switchMap<PlaylistDataSource,
            Status>(playlistDataSourceFactory.playlistDataSourceLive, PlaylistDataSource::state)

    fun retry() {
        playlistDataSourceFactory.playlistDataSourceLive.value?.retry()
    }

    fun listIsEmpty(): Boolean {
        return playlists.value?.isEmpty() ?: true
    }
}