package com.example.a66_courseapp_kotlin_firebase

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {


    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var passwordEditText: EditText

    private var progressDialog: ProgressDialog? = null
    private lateinit var profileImageView: ImageView
    private val PICK_IMAGE_REQUEST = 1
    private var selectedImageUri: Uri? = null
    private var isEditing = false

    private val enabledColor by lazy { resources.getColor(R.color.colorEnabledButton) }
    private val disabledColor by lazy { resources.getColor(R.color.colorDisabledButton) }

    private lateinit var editProfileButton: Button
    private lateinit var saveProfileButton: Button
    private lateinit var changeProfileButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)


        // Initialize the ImageViews
        profileImageView = view.findViewById(R.id.profileImageView)
        profileImageView.setImageResource(R.drawable.profilepic)
        profileImageView.setOnClickListener {
            //selectImage()
        }


        // Initialize butt  ons
        editProfileButton = view.findViewById(R.id.btnEditProfile)
        saveProfileButton = view.findViewById(R.id.btnSaveChanges)
        changeProfileButton = view.findViewById(R.id.btnchange)

        // Set initial colors for buttons
        editProfileButton.setBackgroundColor(enabledColor)
        saveProfileButton.setBackgroundColor(disabledColor)

        // Initialize EditText fields
        firstNameEditText = view.findViewById(R.id.firstNameEditText)
        lastNameEditText = view.findViewById(R.id.lastNameEditText)
        emailEditText = view.findViewById(R.id.emailEditText)
        phoneEditText = view.findViewById(R.id.phoneEditText)
        passwordEditText = view.findViewById(R.id.passwordEditText)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()


        setEditTextsEditable(false)
        editProfileButton.setOnClickListener {
//            isEditing = !isEditing
            setEditMode(!isEditing)
        }

        saveProfileButton.setOnClickListener {
            // updateUserData()
        }


        checkAndFetchUserInfo()


        return view
    }



    private fun checkAndFetchUserInfo() {

        // Check if the user is logged in with Firebase
        val firebaseUser = FirebaseAuth.getInstance().currentUser

        if (firebaseUser != null) {

            // User is logged in with Firebase User logged in with email/password, fetch and display Firebase user info
            firestore = FirebaseFirestore.getInstance()
            val userID = FirebaseAuth.getInstance().currentUser?.uid
            val documentReference= firestore.collection("UserProfileData").document(userID.toString())
            documentReference.get().addOnSuccessListener {
                if (it != null) {
                    firstNameEditText.setText(it.data?.get("firstname")?.toString())
                    lastNameEditText.setText(it.data?.get("lastname")?.toString())
                    phoneEditText.setText(it.data?.get("phone")?.toString())
                    emailEditText.setText(it.data?.get("email")?.toString())

                }
            }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Fetching Failed", Toast.LENGTH_SHORT).show()
                }

        } else {
            // User is not logged in, handle this case as needed
            Log.e(ContentValues.TAG, "User is not logged in.")
        }
    }




    private fun setEditTextsEditable(editable: Boolean) {
        firstNameEditText.isEnabled = editable
        lastNameEditText.isEnabled = editable
        emailEditText.isEnabled = editable
        phoneEditText.isEnabled = editable
        profileImageView.isEnabled = editable
    }


    private fun setEditMode(editMode: Boolean) {
        // Enable or disable EditText fields based on editMode
        setEditTextsEditable(editMode)

        // Enable or disable the Save button based on editMode
        saveProfileButton.isEnabled = editMode

        // Animate button colors
        val fromColor = if (editMode) enabledColor else disabledColor
        val toColor = if (editMode) disabledColor else enabledColor

        animateButtonColor(editProfileButton, fromColor, toColor)
        animateButtonColor(saveProfileButton, toColor, fromColor)
    }


    private fun animateButtonColor(button: Button, fromColor: Int, toColor: Int) {
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), fromColor, toColor)
        colorAnimation.duration = 300 // Adjust the duration as needed
        colorAnimation.addUpdateListener { animator ->
            button.setBackgroundColor(animator.animatedValue as Int)
        }
        colorAnimation.start()
    }





}