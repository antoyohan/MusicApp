package com.example.musicapp.ui.home

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.musicapp.R
import com.example.musicapp.repo.YoutubeService
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

private const val PREF_ACCOUNT_NAME = "accountName"
private val TAG = HomeActivity::class.simpleName

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        var googleAccount: GoogleSignInAccount? =
            intent?.getBundleExtra("ACCOUNT")?.getParcelable("account");

        val accountName = GoogleSignIn.getLastSignedInAccount(this) ?: "test"

        googleAccount?.let {
            YoutubeService.initYoutube(googleAccount, this.applicationContext)
        }
        Log.d(
            TAG,
            "onCreate: ${(accountName as GoogleSignInAccount).givenName}  --  ${googleAccount?.givenName}"
        )
    }
}