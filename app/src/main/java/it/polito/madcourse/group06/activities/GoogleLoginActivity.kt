package it.polito.madcourse.group06.activities

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import it.polito.madcourse.group06.R

class GoogleLoginActivity : AppCompatActivity() {

    private lateinit var account: GoogleSignInAccount
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = firebaseAuth.currentUser

        // if you do not add this check, then you would have to login everytime you start your application on your phone.
        if (GoogleSignIn.getLastSignedInAccount(this) != null) {
            /**
             * This intent will pass to the TBMainActivity the info fetched from Google
             */
            val alreadyLoggedIntent = Intent(this, TBMainActivity::class.java)
            alreadyLoggedIntent.putExtra("id", currentUser?.uid!!)
            alreadyLoggedIntent.putExtra("fullname", currentUser?.displayName!!)
            alreadyLoggedIntent.putExtra("email", currentUser?.email!!)
            startActivity(alreadyLoggedIntent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)
        setContentView(R.layout.activity_google_login)

        // change upper bar colour to orange_poli for login
        window.statusBarColor = this.resources.getColor(R.color.orange_poli)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.orange_poli)));

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id_bis))
            .requestEmail()
            .build()

        // getting the value of gso inside the GoogleSigninClient
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Initialize Firebase Auth variable
        firebaseAuth = Firebase.auth

        val signInButton = findViewById<CardView>(R.id.cardView3) as CardView
        signInButton.setOnClickListener { view: View? ->
            signIn()
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    // onActivityResult() function : this is where we provide the task and data for the Google Account
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                this.account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)

            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.e(TAG, "Google sign in failed", e)
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")

                    /**
                     * This intent will pass to the TBMainActivity the info fetched from Google
                     */
                    val signInIntent = Intent(this, TBMainActivity::class.java)
                    signInIntent.putExtra("id", this.account.id)
                    signInIntent.putExtra("fullname", this.account.displayName)
                    signInIntent.putExtra("email", this.account.email)
                    startActivity(signInIntent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }

    companion object {
        private const val TAG = "GoogleLoginActivity"
        private const val RC_SIGN_IN = 9001
    }
}