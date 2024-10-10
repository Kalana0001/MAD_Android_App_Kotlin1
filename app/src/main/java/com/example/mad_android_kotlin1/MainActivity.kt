package com.example.mad_android_kotlin1

import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private lateinit var userNameTextView: TextView
    private lateinit var userEmailTextView: TextView
    private lateinit var userImageView: ImageView // Add this for your user image

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Auth and Database reference
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("users")

        // Initialize UI components
        drawerLayout = findViewById(R.id.main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        // Get header view to display user data
        val headerView = navigationView.getHeaderView(0)
        userNameTextView = headerView.findViewById(R.id.userNameTextView) // Replace with your actual ID
        userEmailTextView = headerView.findViewById(R.id.userEmailTextView) // Replace with your actual ID
        userImageView = headerView.findViewById(R.id.imageView) // If you want to set a profile image

        // Load user data into the header
        loadUserData()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home_img -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.setting_img -> {
                    replaceFragment(SettingsFragment())
                    true
                }
                R.id.share_img -> {
                    replaceFragment(ShareFragment())
                    true
                }
                R.id.info_img -> {
                    replaceFragment(AboutFragment())
                    true
                }
                else -> false
            }
        }

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
            navigationView.setCheckedItem(R.id.nav_home)
        }
    }

    private fun loadUserData() {
        val user = firebaseAuth.currentUser
        user?.let {
            val email = it.email ?: ""
            userEmailTextView.text = email

            // Query to get user name and other details from database
            database.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (userSnapshot in snapshot.children) {
                            val userName = userSnapshot.child("username").getValue(String::class.java) // Adjust field name as needed
                            userNameTextView.text = userName ?: "Unknown User"
                        }
                    } else {
                        Toast.makeText(this@MainActivity, "No user found with this email", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@MainActivity, "Error fetching user data: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> replaceFragment(HomeFragment())
            R.id.nav_settings -> replaceFragment(SettingsFragment())
            R.id.nav_share -> replaceFragment(ShareFragment())
            R.id.nav_about -> replaceFragment(AboutFragment())
            R.id.nav_logout -> {
                firebaseAuth.signOut()
                Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
                finish() // Close the app or navigate to the sign-in screen
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
