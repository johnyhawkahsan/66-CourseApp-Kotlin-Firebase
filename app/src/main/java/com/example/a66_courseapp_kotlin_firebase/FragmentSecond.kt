package com.example.a66_courseapp_kotlin_firebase

import android.app.ProgressDialog
import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.a66_courseapp_kotlin_firebase.databinding.FragmentSecondBinding
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class FragmentSecond : Fragment() {

    private var _binding: FragmentSecondBinding? = null


    private lateinit var fnameEditText: EditText
    private lateinit var lnameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var registerBtn: Button
    private lateinit var profileImageView: ShapeableImageView
    private lateinit var progressDialog: ProgressDialog



    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        fnameEditText = view.findViewById(R.id.firstNameEditText)
        lnameEditText = view.findViewById(R.id.lastNameEditText)
        emailEditText = view.findViewById(R.id.emailEditText)
        passwordEditText = view.findViewById(R.id.passwordEditText)
        confirmPasswordEditText = view.findViewById(R.id.confirmPasswordEditText)
        phoneEditText = view.findViewById(R.id.phoneEditText)

        profileImageView = view.findViewById(R.id.profileImageView)
        profileImageView.setOnClickListener {
            //selectImage()

        }
        registerBtn = view.findViewById(R.id.buttonRegister)

        binding.buttonRegister.setOnClickListener {

            saveUserData();
            // create a toast message for a successful registration

            // create a snackbar message for a successful registration
            Snackbar.make(view, "Save user data start", Snackbar.LENGTH_SHORT).show()

        }


        binding.backToLogin.setOnClickListener {

            progressDialog = ProgressDialog(requireContext())
            progressDialog.setTitle("Going to Login")
            progressDialog.setMessage("Please wait...")
            progressDialog.show()



            // GlobalScope is better than handler.postDelayed({ }, 2000)
            // Pause the progress dialog for 2 seconds using Kotlin Coroutines
            GlobalScope.launch(Dispatchers.Main) {
                delay(2000) // Pause for 2 seconds

                progressDialog.dismiss() // Dismiss the progress dialog after 5 seconds
                findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
            }



        }





        //Loading
        // how to get context in progress dialog



    }






    private fun saveUserData() {


        val fName = fnameEditText.text.toString().trim()
        val lName = lnameEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()
        val phone = phoneEditText.text.toString().trim()
        val confirmPassword = confirmPasswordEditText.text.toString().trim()



        // Check Email format
        if (fName.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter your First name", Toast.LENGTH_SHORT).show()
            return
        }
        if (lName.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter your Last name", Toast.LENGTH_SHORT).show()
            return
        }
        if (phone.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter your PhoneNo", Toast.LENGTH_SHORT).show()
            return
        }
        if (!phone.matches(Regex("^\\d{11}$"))) {
            // Generate a toast message for an invalid phone number format
            Toast.makeText(requireContext(), "Please enter a valid 11-digit phone number", Toast.LENGTH_SHORT).show()
            return
        }
        if (password.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter your Password", Toast.LENGTH_SHORT).show()
            return
        }
        if (email.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter your Email", Toast.LENGTH_SHORT).show()
            return
        }
        if (confirmPassword.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter your ConfirmPassword", Toast.LENGTH_SHORT).show()
            return
        }
        if (!isEmailValid(email)) {
            Toast.makeText(requireContext(), "Invalid email format", Toast.LENGTH_SHORT).show()
            return
        }
        if (password.length < 6) {
            Toast.makeText(requireContext(), "Password must be at least 6 characters long", Toast.LENGTH_SHORT)
                .show()
            passwordEditText.requestFocus()
            return
        }
        // check Match Password
        if (password != confirmPassword) {
            Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
            passwordEditText.requestFocus()
            return
        }








 /*


        // Check if email already exists in Firestore
        checkEmailExists(email)

        // Create a unique filename for the image
        val imageFileName = "${auth.currentUser?.uid}_${System.currentTimeMillis()}.jpg"
        // Get a reference to the profile image storage location
        val profileImageRef = storage.reference.child("profile_images/$imageFileName")


        // Upload the selected image
        val selectedImageUri = profileImageView.tag as Uri? // Tag holds the selected image URI
        progressDialog.show()


        if (selectedImageUri != null) {
            val uploadTask = profileImageRef.putFile(selectedImageUri)
            uploadTask.addOnSuccessListener { taskSnapshot ->
                // Image upload successful, get the download URL
                profileImageRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()

                    // Continue with user registration and profile data storage
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(requireContext()) { task ->
                            if (task.isSuccessful) {
                                // Registration successful
                                progressDialog.dismiss()
                                Toast.makeText(
                                    requireContext(),
                                    "Account Created successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val userID = FirebaseAuth.getInstance().currentUser?.uid
                                val documentReference = firestore.collection("UserProfileData")
                                    .document(userID.toString())
                                val user = mutableMapOf<String, Any>()
                                user["firstname"] = fName
                                user["lastname"] = lName
                                user["email"] = email
                                user["phone"] = phone
                                user["profile_image_url"] = imageUrl // Store the image URL
                                documentReference.set(user).addOnSuccessListener {
                                    Log.d(ContentValues.TAG, "User profile is created for $userID")
                                }.addOnFailureListener {
                                    Log.d(
                                        ContentValues.TAG,
                                        "Failed to Created"
                                    )
                                }
                                navigateToLoginPage()
                            } else {
                                progressDialog.dismiss()
                                // Registration failed
                                val errorMessage = task.exception?.message ?: "Registration failed"
                                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
            uploadTask.addOnFailureListener { exception: Exception ->
                progressDialog.dismiss()
                // Handle image upload failure
                Toast.makeText(requireContext(), "Failed to upload profile image", Toast.LENGTH_SHORT).show()

            }
        } else {
            progressDialog.dismiss()
            // Handle the case where no image was selected
            Toast.makeText(requireContext(), "Please select a profile image", Toast.LENGTH_SHORT).show()

        }




        */




    }




    // ... (isEmailValid, selectImage, resultLauncher, clearFields, backToLogin)
    private fun isEmailValid(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }




    private fun checkEmailExists(email: String) {

/*
        firestore.collection("UserProfileData")
            .whereEqualTo("email", email)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (!task.result.isEmpty) {
                        progressDialog.dismiss()
                        Toast.makeText(this, "Email already exists", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    progressDialog.dismiss()
                    // Error occurred while checking email existence
                    Toast.makeText(this, "Error checking email existence", Toast.LENGTH_SHORT).show()
                }
            }
        */

    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}