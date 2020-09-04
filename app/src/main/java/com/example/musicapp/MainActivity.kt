package com.example.musicapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.musicapp.repo.YoutubeService
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import com.google.api.services.youtube.YouTube
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


private val TAG = MainActivity::class.simpleName
private val REQUEST_AUTHORIZATION = 1001

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val googleAccount: GoogleSignInAccount? =
            intent?.getBundleExtra("ACCOUNT")?.getParcelable("account");
        googleAccount?.let {
            YoutubeService.initYoutube(googleAccount, this.applicationContext)
            getData()
        }
    }

    private fun getData() {
        CoroutineScope(Dispatchers.IO).launch {
            getYoutubeApi(YoutubeService.youTube)
        }
    }

    private fun isGooglePlayServicesAvailable(): Boolean {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(this)
        return connectionStatusCode == ConnectionResult.SUCCESS
    }

    private suspend fun getYoutubeApi(youTube: YouTube) {
        try {

            val playlist = youTube.playlists().list("snippet,contentDetails")
                .setMaxResults(10)
                .setMine(true).execute()

            val playlistDetails = youTube.playlistItems().list("contentDetails, snippet")
                .setPlaylistId(playlist.items.get(0).id).execute()
            Log.d(TAG, "getYoutubeApi: " + playlist)
        } catch (ex: UserRecoverableAuthIOException) {
            Log.e(TAG, "getYoutubeApi:  exception caught")
            startActivityForResult(
                (ex as UserRecoverableAuthIOException).intent,
                REQUEST_AUTHORIZATION
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_AUTHORIZATION -> getData()
        }
    }

}