package com.example.musicapp.ui.home.playlist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R
import com.example.musicapp.model.PlaylistData
import com.example.musicapp.ui.recycler.CellClickListener
import com.example.musicapp.ui.recycler.PlaylistRecyclerAdapter
import com.example.musicapp.utils.State
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import com.google.api.services.youtube.model.Playlist
import kotlinx.android.synthetic.main.playlist_fragment.*

private const val TAG = "PlaylistFragment"
class PlaylistFragment : Fragment() {
    private val REQUEST_AUTHORIZATION = 1001
    companion object {
        fun newInstance() =
            PlaylistFragment()
    }

    private lateinit var adapter: PlaylistRecyclerAdapter
    private lateinit var viewModel: PlaylistViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.playlist_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PlaylistViewModel::class.java)
        initRecyclerView()

    }

    private fun initRecyclerView() {
        playlist_list.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        adapter = PlaylistRecyclerAdapter()
        playlist_list.adapter = adapter
        viewModel.playlists.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        viewModel.getState().observe(viewLifecycleOwner, Observer { status ->
            center_progressbar.visibility =
                if (viewModel.listIsEmpty() && status.state == State.LOADING) View.VISIBLE else View.GONE
            adapter.setState(status.state ?: State.DONE)
            if (status.state == State.ERROR && status.reason is UserRecoverableAuthIOException) {
                startActivityForResult(
                    (status.reason as UserRecoverableAuthIOException).intent,
                    REQUEST_AUTHORIZATION
                )
            }
        })

        adapter.onCellClickListener = object : CellClickListener<Playlist> {
            override fun onCellClickListener(data: Playlist) {
                Log.d(TAG, "onCellClickListener: ${data.snippet.title}")
                val playlistData = PlaylistData(
                    data.id,
                    data.snippet.title,
                    data.snippet.thumbnails.high.url,
                    data.contentDetails.itemCount
                )
               val action =  PlaylistFragmentDirections.actionPlaylistFragmentToVideoListFragment(playlistData)
               findNavController().navigate(action)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_AUTHORIZATION -> viewModel.retry()
        }
    }

}