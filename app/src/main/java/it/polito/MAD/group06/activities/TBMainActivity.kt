package it.polito.MAD.group06.activities

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import it.polito.MAD.group06.R
import it.polito.MAD.group06.databinding.ActivityMainBinding
import it.polito.MAD.group06.models.advertisement.Advertisement
import it.polito.MAD.group06.utilities.getBitmapFromFile
import it.polito.MAD.group06.viewmodels.AdvertisementViewModel
import it.polito.MAD.group06.viewmodels.UserProfileViewModel

class TBMainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private val usrVM by viewModels<UserProfileViewModel>()
    private val advVM by viewModels<AdvertisementViewModel>()

    // declare the GoogleSignInClient
    lateinit var mGoogleSignInClient: GoogleSignInClient

    // val auth is initialized by lazy
    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // configure the GoogleSignInOptions with the same server client ID used for logging in
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id_bis))
            .requestEmail()
            .build()
        mGoogleSignInClient= GoogleSignIn.getClient(this, gso)

        // inflate the view hierarchy
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.ShowListTimeslots,
                R.id.ShowListOfServices,
                R.id.showProfileFragment
            ), drawerLayout
        )
        // setup navigation drawer
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Navigation view item click listener
        navView.setNavigationItemSelectedListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            when (it.itemId) {
                R.id.ShowListOfServicesMenuItem->{
                    navController.navigate(R.id.ShowListOfServices)
                    true
                }
                R.id.ShowListOfAdvertisementsMenuItem -> {
                    navController.navigate(R.id.ShowListTimeslots)
                    true
                }
                R.id.ShowProfileMenuItem -> {
                    navController.navigate(R.id.showProfileFragment)
                    true
                }
                R.id.logOutMenuItem -> {
                    // log out from Google and go back to log in activity
                    mGoogleSignInClient.signOut().addOnCompleteListener {
                        val intent = Intent(this, GoogleLoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    true
                }
                else -> false
            }
        }

        val fullnameHeader = navView.getHeaderView(0).findViewById<TextView>(R.id.fullname_header)
        val nicknameHeader = navView.getHeaderView(0).findViewById<TextView>(R.id.nickname_header)
        val pictureHeader = navView.getHeaderView(0).findViewById<ImageView>(R.id.picture_header)

        usrVM.profile.observe(this) { user ->
            if (user != null) {
                fullnameHeader.text = user.fullName
                nicknameHeader.text = "@${user.nickname}"
                val profilePicturePath = getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + '/' + resources.getString(
                    R.string.profile_picture_filename
                )

                getBitmapFromFile(profilePicturePath)?.also {
                    pictureHeader.setImageBitmap(it)
                }
            } else {
                fullnameHeader.text = "Guido Saracco"
                nicknameHeader.text = "@rettore"
                pictureHeader.setImageResource(R.drawable.propic)

            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}