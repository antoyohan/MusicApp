package com.example.musicapp.utils

enum class State {
    DONE, LOADING, ERROR
}

class Status(val state: State, exception: java.lang.Exception? = null) {
    var reason:Exception? = exception
}