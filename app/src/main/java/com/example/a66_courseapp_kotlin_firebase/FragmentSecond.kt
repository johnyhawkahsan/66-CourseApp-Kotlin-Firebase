package com.example.a66_courseapp_kotlin_firebase

import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * SignUp fragment
 */
class FragmentSecond : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

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

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()


        //Loading
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Uploading")
        progressDialog.setMessage("Please wait...")
        progressDialog.setCancelable(false) // Prevent dismissing by tapping outside


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

            // create a snackbar message for a successful registration
            Snackbar.make(view, "Save user data start", Snackbar.LENGTH_SHORT).show()

        }




        // When back to login button clicked, user is redirected to Login Fragment
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




        // Check if email already exists in Firestore
        checkEmailExists(email)


        progressDialog.show()


        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {

                    // Registration successful
                    progressDialog.dismiss()
                    Toast.makeText(requireContext(), "Account Created successfully", Toast.LENGTH_SHORT).show()

                    // we are saving user data in Firestore - Collection name is "UserProfileData"
                    val userID = FirebaseAuth.getInstance().currentUser?.uid
                    val documentReference = firestore.collection("UserProfileData")
                        .document(userID.toString())
                    val user = mutableMapOf<String, Any>()
                    user["firstname"] = fName
                    user["lastname"] = lName
                    user["email"] = email
                    user["phone"] = phone
                    user["admin"] = false // this is to set by default if a user is an admin

                    documentReference.set(user).addOnSuccessListener {
                        Log.d(ContentValues.TAG, "User profile data is created for $userID")
                    }.addOnFailureListener {
                        Log.d(
                            ContentValues.TAG,
                            "Failed to Save data to Firestore"
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




    // ... (isEmailValid, selectImage, resultLauncher, clearFields, backToLogin)
    private fun isEmailValid(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }



    // Function to check if an email is already registered got this from ChatGPT
    fun checkEmailExists(email: String) {
        auth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (!task.result.toString().isEmpty()) {
                        progressDialog.dismiss()
                        Toast.makeText(requireActivity(), "Success", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    progressDialog.dismiss()
                    // Error occurred while checking email existence
                    Toast.makeText(requireActivity(), "Error checking email existence", Toast.LENGTH_SHORT).show()
                }
            }
    }








    private fun navigateToLoginPage() {
        val intent = Intent(requireActivity(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()  // Close the SignUp activity to prevent going back to it.
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}