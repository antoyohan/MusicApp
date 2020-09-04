package com.example.musicapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.musicapp.R
import com.example.musicapp.ui.home.HomeActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_login.*

/**
 * LoginActivity handles the SignIn of the users.
 * The initial screen in the App.
 * */

private const val TAG = "LoginActivity"

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initSigninButton()
        configureGoogleSignIn()
    }

    private fun initSigninButton() {
        signin_button.setSize(SignInButton.SIZE_WIDE)
        signin_button.setOnClickListener(this)
    }

    private fun configureGoogleSignIn() {
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)
        //updateUI(account)
    }

    private fun updateUI(account: GoogleSignInAccount?) {
        account?.let {
            signin_button.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            Handler().postDelayed({
                launchMainActivity(account)
                finish()
            }, 2000)
        }
    }

    private fun launchMainActivity(account: GoogleSignInAccount) {
        val intent = Intent(this, HomeActivity::class.java)
        val bundle = Bundle()
        bundle.putParcelable("account", account)
        intent.putExtra("ACCOUNT", bundle)
        startActivity(intent)
    }

    /**
     * Google signIn activity start
     * */
    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.signin_button -> signIn()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account =
                completedTask.getResult(ApiException::class.java)
            Log.d(TAG, "handleSignInResult: " + account?.email)
            updateUI(account)
        } catch (e: ApiException) {
            Log.d(TAG, "signInResult:failed code=" + e.statusCode)
            updateUI(null)
        }
    }
}