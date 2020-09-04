package com.example.musicapp.ui.home.playlist

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
import com.google.api.services.youtube.model.Playlist
import kotlinx.android.synthetic.main.playlist_fragment.*

private const val TAG = "PlaylistFragment"
class PlaylistFragment : Fragment() {

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

        viewModel.getState().observe(viewLifecycleOwner, Observer { state ->
            center_progressbar.visibility =
                if (viewModel.listIsEmpty() && state == State.LOADING) View.VISIBLE else View.GONE
            // txt_error.visibility = if (viewModel.listIsEmpty() && state == State.ERROR) View.VISIBLE else View.GONE
            if (!viewModel.listIsEmpty()) {
                adapter.setState(state ?: State.DONE)
            }
        })

        adapter.onCellClickListener = object : CellClickListener<Playlist> {
            override fun onCellClickListener(data: Playlist) {
                Log.d(TAG, "onCellClickListener: ${data.snippet.title}")

                var playlistData = PlaylistData(
                    data.id,
                    data.snippet.title,
                    data.snippet.thumbnails.default.toString(),
                    data.contentDetails.itemCount
                )

               val action =  PlaylistFragmentDirections.actionPlaylistFragmentToVideoListFragment(playlistData)

                //bundle.putBundle("data", playlistData)
               findNavController().navigate(action)
            }

        }
    }

}