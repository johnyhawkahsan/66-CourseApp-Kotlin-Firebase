package com.example.a66_courseapp_kotlin_firebase

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/** A simple [Fragment] subclass. Use the [WelcomeFragment.newInstance] factory method to create an instance of this fragment. */
class WelcomeFragment : Fragment() {


    private lateinit var welcomeToProfileTextView: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore




    private var param1: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }

        // log to display both param1 and param2
        Log.d(WelcomeFragment.toString(), "Params data recevied = " + param1.toString() )

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_welcome, container, false)

        // Inflate the layout for this fragment
        welcomeToProfileTextView = view.findViewById(R.id.welcometoprofile)
        welcomeToProfileTextView.setText(param1)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        checkAndFetchUserInfo()


        // recycler view
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(view.context)
        recyclerView.layoutManager = layoutManager


        val videoList = mutableListOf(
            Video("Amazon FBA Course", "https://img.youtube.com/vi/mCqh9sV3hm8/default.jpg", "https://www.youtube.com/watch?v=mCqh9sV3hm8"),
            Video("Video 2", "https://img.youtube.com/vi/fis26HvvDII/default.jpg", "https://www.youtube.com/watch?v=fis26HvvDII"),
            Video("Video 2", "https://img.youtube.com/vi/fis26HvvDII/default.jpg", "https://www.youtube.com/watch?v=fis26HvvDII"),
            Video("Video 2", "https://img.youtube.com/vi/fis26HvvDII/default.jpg", "https://www.youtube.com/watch?v=fis26HvvDII"),
            Video("Amazon FBA Course", "https://img.youtube.com/vi/mCqh9sV3hm8/default.jpg", "https://www.youtube.com/watch?v=mCqh9sV3hm8"),
            Video("Video 2", "https://example.com/thumbnail2.jpg", "https://www.youtube.com/watch?v=VIDEO_ID_2"),
            // Add more video items as needed
        )

        val adapter = VideoAdapter(videoList)
        recyclerView.adapter = adapter


        return view
    }



    private fun checkAndFetchUserInfo() {
        // Check if the user is logged in with Firebase
        val firebaseUser = FirebaseAuth.getInstance().currentUser

        if (firebaseUser != null) {
            // User is logged in with Firebase
            if (firebaseUser.providerData.any { it.providerId == GoogleAuthProvider.PROVIDER_ID }) {
                // User logged in with Google, fetch and display Google user info
                // fetchAndDisplayUserInfoGoogle()
            } else if (firebaseUser.providerData.any { it.providerId == FacebookAuthProvider.PROVIDER_ID }) {
                // User logged in with Facebook, fetch and display Facebook user info
                // fetchAndDisplayUserInfoFacebook()
            } else {
                // User logged in with email/password, fetch and display Firebase user info
                fetchAndDisplayUserInfoFirebase()
            }
        } else {
            // User is not logged in, handle this case as needed
            Log.e(ContentValues.TAG, "User is not logged in.")
        }
    }




    //------ Fetch --!
    private fun fetchAndDisplayUserInfoFirebase(){
        firestore = FirebaseFirestore.getInstance()
        val userID = FirebaseAuth.getInstance().currentUser?.uid
        val documentReference= firestore.collection("UserProfileData").document(userID.toString())
        documentReference.get().addOnSuccessListener {
            Toast.makeText(requireContext(), "Fetching Successful", Toast.LENGTH_SHORT).show()

        }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Fetching Failed", Toast.LENGTH_SHORT).show()
            }
    }



    // use this code to pass params to a fuction where we create our fragment. When we create it, we will need to pass params to it
    companion object {
        fun newInstance(param1: String) =
            WelcomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }



}