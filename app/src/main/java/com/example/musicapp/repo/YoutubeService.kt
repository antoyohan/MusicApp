package com.example.musicapp.repo

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.YouTubeScopes

class YoutubeService {

    companion object {
        lateinit var youTube: YouTube
            private set

        fun initYoutube(googleAccount: GoogleSignInAccount, applicationContext: Context) {
            val SCOPES =
                arrayOf(YouTubeScopes.YOUTUBE_READONLY)
            val mCredential = GoogleAccountCredential.usingOAuth2(
                applicationContext, SCOPES.toMutableList()
            ).setBackOff(ExponentialBackOff())
            mCredential.setSelectedAccount(googleAccount.account);
            youTube = YouTube.Builder(
                NetHttpTransport(),
                JacksonFactory(),
                mCredential
            ).setApplicationName("APPNAME").build()
        }
    }


}