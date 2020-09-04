package com.example.musicapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.musicapp.model.PlaylistData
import com.example.musicapp.ui.home.videolist.VideolistViewModel

class MainViewModelFactory<N>(private val args:N):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VideolistViewModel::class.java)) {
            return VideolistViewModel(args as PlaylistData) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}