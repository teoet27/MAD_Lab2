package it.polito.group06.views

import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.google.android.material.navigation.NavigationView
import it.polito.group06.R
import it.polito.group06.databinding.ActivityMainBinding
import it.polito.group06.utilities.getBitmapFromFile
import it.polito.group06.viewmodels.AdvertisementViewModel
import it.polito.group06.viewmodels.UserProfileViewModel


class TBMainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private val usrVM by viewModels<UserProfileViewModel>()
    private val advVM by viewModels<AdvertisementViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                R.id.showProfileFragment,
                R.id.showSingleTimeslot
            ), drawerLayout
        )
        // setup navigation drawer
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Navigation view item click listener
        navView.setNavigationItemSelectedListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            when (it.itemId) {
                R.id.ShowListOfAdvertisementsMenuItem -> {
                    navController.navigate(R.id.ShowListTimeslots)
                    //replaceFragment(Frag5ShowListTimeslots())
                    true
                }
                R.id.ShowProfileMenuItem -> {
                    navController.navigate(R.id.showProfileFragment)
                    //replaceFragment(ShowProfileFragment())
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
                val profilePicturePath = getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + '/' + resources.getString(R.string.profile_picture_filename)

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

    // Extension function to replace fragment
    private fun AppCompatActivity.replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.nav_host_fragment_content_main, fragment)
            .commit()
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