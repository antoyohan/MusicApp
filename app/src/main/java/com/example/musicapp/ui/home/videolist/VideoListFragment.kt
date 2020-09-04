package com.example.musicapp.ui.home.videolist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R
import com.example.musicapp.model.PlaylistData
import com.example.musicapp.ui.MainViewModelFactory
import com.example.musicapp.ui.recycler.CellClickListener
import com.example.musicapp.ui.recycler.PlaylistItemRecyclerAdapter
import com.example.musicapp.utils.State
import com.google.api.services.youtube.model.PlaylistItem
import kotlinx.android.synthetic.main.fragment_video_list.*
import kotlinx.android.synthetic.main.playlist_fragment.*

private const val TAG = "VideoListFragment"

class VideoListFragment : Fragment() {

    private lateinit var adapter: PlaylistItemRecyclerAdapter
    private lateinit var viewModelFactory: MainViewModelFactory<PlaylistData>
    private lateinit var viewModel: VideolistViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_video_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playlistData = arguments?.let { VideoListFragmentArgs.fromBundle(it).playlist }
        Log.d(TAG, "onViewCreated: ${playlistData?.name}")
        viewModelFactory = MainViewModelFactory<PlaylistData>(playlistData as PlaylistData)
        viewModel = ViewModelProvider(this, viewModelFactory).get(VideolistViewModel::class.java)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        video_list.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        adapter = PlaylistItemRecyclerAdapter()
        video_list.adapter = adapter
        viewModel.playlists.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        viewModel.getState().observe(viewLifecycleOwner, Observer { state ->
            /*center_progressbar.visibility =
                if (viewModel.listIsEmpty() && state == State.LOADING) View.VISIBLE else View.GONE*/
            // txt_error.visibility = if (viewModel.listIsEmpty() && state == State.ERROR) View.VISIBLE else View.GONE
            if (!viewModel.listIsEmpty()) {
                adapter.setState(state ?: State.DONE)
            }
        })

        adapter.onCellClickListener = object : CellClickListener<PlaylistItem> {
            override fun onCellClickListener(data: PlaylistItem) {
                Log.d(TAG, "onCellClickListener: ${data.snippet.title}")

            }

        }
    }
}