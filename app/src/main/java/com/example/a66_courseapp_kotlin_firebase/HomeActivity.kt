package com.example.a66_courseapp_kotlin_firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    // Menu Bar
    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // create a log message that activity has successfully created
        Log.d("HomeActivity", "onCreate")





        //Menu Bar
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, 0, 0
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)


        setToolbarTitle("Home")

        // sending params just for fun. If you don't want to send, don't need to create new instance, just create new fragment = supportFragmentManager.beginTransaction()
        val welcomeFragment = WelcomeFragment.newInstance("Welcome to Welcome Fragment")
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, welcomeFragment)
            //.addToBackStack(null) // Optional: Add to back stack for navigation
            .commit()


        // Old code
        //val fragment= supportFragmentManager.beginTransaction()
        //fragment.replace(R.id.fragment_container,WelcomeFragment()).commit()




    }

    //------------------------   Menu Bar -----------------------------------------
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_profile -> {
                setToolbarTitle("Profile")
/*                val fragment = ProfileFragment()
                val fragmentManager = supportFragmentManager
                fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
                */

            }
            R.id.nav_home -> {
                setToolbarTitle("Home")
/*                val fragment = WelcomFragment()
                val fragmentManager = supportFragmentManager
                fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
                */
            }
            R.id.nav_logout -> {
                signOut()
            }
            R.id.nav_deleteAccount -> {
                // DeleteAccount()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


    fun setToolbarTitle(title: String){
        supportActionBar?.title=title
    }





    private fun signOut() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Logout")
        alertDialog.setMessage("Are you sure you want to logout?")
        alertDialog.setPositiveButton("Yes") { _, _ ->

            navigateToLogin()
            Toast.makeText(this, "Signed Out", Toast.LENGTH_SHORT).show()
        }
        alertDialog.setNegativeButton("No", null) // User clicked "No," do nothing
        alertDialog.show()
    }



    private fun navigateToLogin() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Close the profile activity to prevent returning to it.
    }


}