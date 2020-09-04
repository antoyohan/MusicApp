package com.example.musicapp.repo.videolist

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.musicapp.repo.YoutubeService
import com.example.musicapp.utils.State
import com.google.api.services.youtube.model.PlaylistItem
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val TAG = "PlayLisItemDataSource"

class PlayLisItemDataSource(
    private val compositeDisposable: CompositeDisposable,
    private val id: String
) :
    PageKeyedDataSource<String, PlaylistItem>() {
    var state: MutableLiveData<State> = MutableLiveData()
    private var retryCompletable: Completable? = null

    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, PlaylistItem>
    ) {

        updateState(State.LOADING)
        CoroutineScope(Dispatchers.IO).launch {
            val playlistItems =
                YoutubeService.youTube.playlistItems().list("contentDetails, snippet")
                    .setMaxResults(params.requestedLoadSize.toLong())
                    .setPlaylistId(id).execute()
            Log.d(TAG, "loadInitial: ${playlistItems.items.size}")
            withContext(Dispatchers.Main) {
                updateState(State.DONE)
                callback.onResult(
                    playlistItems.items,
                    playlistItems.prevPageToken,
                    playlistItems.nextPageToken
                )
            }
        }
    }

    override fun loadAfter(
        params: LoadParams<String>,
        callback: LoadCallback<String, PlaylistItem>
    ) {
        updateState(State.LOADING)

        CoroutineScope(Dispatchers.IO).launch {
            val playlistItems =
                YoutubeService.youTube.playlistItems().list("contentDetails, snippet")
                    .setMaxResults(params.requestedLoadSize.toLong())
                    .setPageToken(params.key)
                    .setPlaylistId(id).execute()
            Log.d(TAG, "loadAfter: ${playlistItems.items.size}   ${params.key}")

            withContext(Dispatchers.Main) {
                updateState(State.DONE)
                callback.onResult(playlistItems.items, playlistItems.nextPageToken)
            }
        }
    }

    override fun loadBefore(
        params: LoadParams<String>,
        callback: LoadCallback<String, PlaylistItem>
    ) {

        updateState(State.LOADING)
        CoroutineScope(Dispatchers.IO).launch {
            val playlist = YoutubeService.youTube.playlistItems().list("snippet,contentDetails")
                .setMaxResults(params.requestedLoadSize.toLong())
                .setPageToken(params.key)
                .execute()
            Log.d(TAG, "loadBefore: ${playlist.items.size}   ${params.key}")

            withContext(Dispatchers.Main) {
                updateState(State.DONE)
                callback.onResult(playlist.items, playlist.prevPageToken)
            }
        }
    }

    private fun updateState(state: State) {
        this.state.postValue(state)
    }
}