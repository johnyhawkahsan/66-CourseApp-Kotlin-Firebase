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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    // Menu Bar
    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView

    // Initialize Firebase Auth
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // create a log message that activity has successfully created
        Log.d("HomeActivity", "onCreate")




        //Menu Bar
        toolbar = findViewById(R.id.toolbar) // toolbar is located in content_main_second.xml
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout) // drawer layout is located in activity_home.xml
        navView = findViewById(R.id.nav_view) // navigation view is located in activity_home.xml


        // Construct a new ActionBarDrawerToggle with a Toolbar.
        //The given Activity will be linked to the specified DrawerLayout and the Toolbar's navigation icon will be set to a custom drawable. Using this constructor will set Toolbar's navigation click listener to toggle the drawer when it is clicked.
        //This drawable shows a Hamburger icon when drawer is closed and an arrow when drawer is open. It animates between these two states as the drawer opens
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, 0, 0
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)


        setToolbarTitle("Home")

        // sending params just for fun. If you don't want to send, don't need to create new instance, just create new fragment = supportFragmentManager.beginTransaction()
        val welcomeFragment = WelcomeFragment.newInstance("Welcome to Course App")
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
                val fragment = ProfileFragment()
                val fragmentManager = supportFragmentManager
                fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()


            }
            R.id.nav_home -> {
                setToolbarTitle("Home")
                val fragment = WelcomeFragment()
                val fragmentManager = supportFragmentManager
                fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()

            }
            R.id.nav_logout -> {
                signOut()
            }
            R.id.nav_deleteAccount -> {
                DeleteAccount()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


    fun setToolbarTitle(title: String){
        supportActionBar?.title=title
    }






    // Delete user account from Firebase
    fun DeleteAccount() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Delete")
        alertDialog.setMessage("Are you sure you want to Delete your Account?")
        alertDialog.setPositiveButton("Yes") { _, _ ->
            // Get the current user.
            val user = Firebase.auth.currentUser
            // Delete the user from Firebase Authentication.
            user?.delete()?.addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    // The user has been deleted from Firebase Authentication. Delete the user's data from Firestore.
                    auth.signOut()

                    // The user's data has been deleted from Firestore.
                    Toast.makeText(this, "Account deletion complete: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    navigateToLogin()

                } else {
                    // An error occurred while deleting the user from Firebase Authentication.
                    Toast.makeText(this, "An error occurred while deleting the user from Firebase Authentication", Toast.LENGTH_SHORT).show()
                }
            }
        }
        alertDialog.setNegativeButton("No", null) // User clicked "No," do nothing
        alertDialog.show()
    }



    private fun signOut() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Logout")
        alertDialog.setMessage("Are you sure you want to logout?")
        alertDialog.setPositiveButton("Yes") { _, _ ->

            Firebase.auth.signOut()

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