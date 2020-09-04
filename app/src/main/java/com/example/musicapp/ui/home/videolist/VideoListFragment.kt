package com.example.musicapp.ui.home.videolist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.musicapp.R

private const val TAG = "VideoListFragment"

class VideoListFragment : Fragment() {


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
    }
}