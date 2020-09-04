package com.example.musicapp.repo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.musicapp.utils.State
import com.example.musicapp.utils.Status
import com.google.api.services.youtube.model.Playlist
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "PlaylistDataSource"
class PlaylistDataSource(
    private val compositeDisposable: CompositeDisposable
) :
    PageKeyedDataSource<String, Playlist>() {
    var state: MutableLiveData<Status> = MutableLiveData()
    private var retryCompletable:(()->Any)? = null

    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, Playlist>
    ) {

        updateState(State.LOADING)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val playlist = YoutubeService.youTube.playlists().list("snippet,contentDetails")
                    .setMaxResults(params.requestedLoadSize.toLong())
                    .setMine(true).execute()
                Log.d(TAG, "loadInitial: ${playlist.items.size}")
                withContext(Dispatchers.Main) {
                    updateState(State.DONE)
                    callback.onResult(
                        playlist.items,
                        playlist.prevPageToken,
                        playlist.nextPageToken
                    )
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d(TAG, "loadInitial: exception caught")
                    updateState(State.ERROR, ex)
                    setRetry( {loadInitial(params, callback)})
                }
            }
        }
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, Playlist>) {
        updateState(State.LOADING)

        CoroutineScope(Dispatchers.IO).launch {
            val playlist = YoutubeService.youTube.playlists().list("snippet,contentDetails")
                .setMaxResults(params.requestedLoadSize.toLong())
                .setPageToken(params.key)
                .setMine(true).execute()
            Log.d(TAG, "loadAfter: ${playlist.items.size}   ${params.key}")

            withContext(Dispatchers.Main) {
                updateState(State.DONE)
                callback.onResult(playlist.items, playlist.nextPageToken)
            }
        }
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, Playlist>) {

        updateState(State.LOADING)
        CoroutineScope(Dispatchers.IO).launch {
            val playlist = YoutubeService.youTube.playlists().list("snippet,contentDetails")
                .setMaxResults(params.requestedLoadSize.toLong())
                .setPageToken(params.key)
                .setMine(true).execute()
            Log.d(TAG, "loadBefore: ${playlist.items.size}   ${params.key}")

            withContext(Dispatchers.Main) {
                updateState(State.DONE)
                callback.onResult(playlist.items, playlist.prevPageToken)
            }
        }
    }

    private fun updateState(state: State, reason: Exception? = null) {
        this.state.postValue(Status(state, reason))
    }


    fun retry() {
        Log.d(TAG, "retry: ")
        retryCompletable?.let { it1 -> it1() }
    }

    private fun setRetry(action: ()-> Any) {
        retryCompletable = action
    }
}