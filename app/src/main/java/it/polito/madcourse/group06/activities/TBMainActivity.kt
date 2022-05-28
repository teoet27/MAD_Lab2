package it.polito.madcourse.group06.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import it.polito.madcourse.group06.databinding.ActivityMainBinding
import it.polito.madcourse.group06.viewmodels.SharedViewModel
import it.polito.madcourse.group06.viewmodels.UserProfileViewModel
import it.polito.madcourse.group06.R
import it.polito.madcourse.group06.utilities.DrawerInterface

class TBMainActivity : AppCompatActivity(), DrawerInterface {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private val userProfileViewModel: UserProfileViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

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
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        val fullname = intent.getStringExtra("fullname")
        val email = intent.getStringExtra("email")

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
                R.id.showProfileFragment,
                R.id.newProfileFragment,
                R.id.showListOfUsers
            ), drawerLayout
        )
        // setup navigation drawer
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        userProfileViewModel.setCurrentUserProfile(fullname!!, email!!)
        navController.navigate(R.id.newProfileFragment)

        // Navigation view item click listener
        navView.setNavigationItemSelectedListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            when (it.itemId) {
                R.id.ShowListOfServicesMenuItem -> {
                    navController.navigate(R.id.ShowListOfServices)
                    true
                }
                R.id.ShowListOfAdvertisementsMenuItem -> {
                    sharedViewModel.resetSearchState()
                    navController.navigate(
                        R.id.ShowListTimeslots,
                        bundleOf("selected_skill" to "All")
                    )
                    true
                }
                R.id.ShowProfileMenuItem -> {
                    navController.navigate(R.id.showProfileFragment)
                    true
                }
                R.id.ListOfUsers -> {
                    navController.navigate(R.id.showListOfUsers)
                    true
                }
                R.id.settingID -> {
                    navController.navigate(R.id.setting)
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

        userProfileViewModel.currentUser.observe(this) { user ->
            fullnameHeader.text = user.fullName
            nicknameHeader.text = "@${user.nickname}"
            // Profile Picture
            if (user.imgPath.isNullOrEmpty()) {
                userProfileViewModel.retrieveStaticProfilePicture(pictureHeader)
            } else {
                userProfileViewModel.retrieveProfilePicture(pictureHeader, user.imgPath!!)
            }
        }
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

    override fun drawerLock() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    override fun drawerUnlock() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }

}