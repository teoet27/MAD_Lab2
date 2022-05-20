package it.polito.MAD.group06.activities

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.activityViewModels
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import it.polito.MAD.group06.R
import it.polito.MAD.group06.utilities.GoogleLoginSavedPreferencesObject
import it.polito.MAD.group06.viewmodels.UserProfileViewModel

class GoogleLoginActivity : AppCompatActivity() {

    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var account: GoogleSignInAccount
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    /*
    waiting for this to be a fragment
    private val usrViewModel: UserProfileViewModel by activityViewModels()
     */

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = firebaseAuth.currentUser
        updateUI(currentUser)

        // if you do not add this check, then you would have to login everytime you start your application on your phone.
        if (GoogleSignIn.getLastSignedInAccount(this) != null) {
            startActivity(Intent(this, TBMainActivity::class.java))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)
        setContentView(R.layout.activity_google_login)


        // Define ActionBar object to change colour
        val actionBar: ActionBar? = supportActionBar
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
                /*
                waiting for this to be a fragment
                userViewModel.setCurrentUserProfile(account)
                userViewModel.currentUser.observe(this) {
                    Log.e("userprofile", it!!.toString())
                }
                */
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
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = firebaseAuth.currentUser
                    updateUI(user)

                    // start main activity
                    val intent = Intent(this, TBMainActivity::class.java)
                    intent.putExtra("id", this.account.id)
                    intent.putExtra("fullname", this.account.displayName)
                    intent.putExtra("email", this.account.email)
                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            GoogleLoginSavedPreferencesObject.setEmail(this, user.email.toString())
            GoogleLoginSavedPreferencesObject.setUsername(this, user.displayName.toString())
        } else {
            GoogleLoginSavedPreferencesObject.setEmail(this, "")
            GoogleLoginSavedPreferencesObject.setUsername(this, "")
        }
    }

    companion object {
        private const val TAG = "GoogleLoginActivity"
        private const val RC_SIGN_IN = 9001
    }
}