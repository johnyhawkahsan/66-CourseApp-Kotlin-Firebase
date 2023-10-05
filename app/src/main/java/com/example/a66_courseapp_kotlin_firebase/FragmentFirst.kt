package com.example.a66_courseapp_kotlin_firebase

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.example.a66_courseapp_kotlin_firebase.databinding.FragmentFirstBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FragmentFirst : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // The lateinit keyword is short for "late initialization." It allows you to declare properties (usually mutable variables) without initializing them immediately. This is particularly useful when working with Android views because you typically find and initialize views within the onCreate method or after setContentView has been called in an Activity or Fragment.
    // Deferred Initialization: You don't need to initialize these properties immediately, which can be helpful when you want to find views in your layout hierarchy after it has been inflated.
    // Null Safety: Since you declare these properties as non-nullable (i.e., without a ?), you promise that you'll initialize them before using them.
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginTosignUp: TextView
    private lateinit var adminLoginBtn: Button


    private lateinit var builder: AlertDialog.Builder
    private lateinit var progressDialog: ProgressDialog

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emailEditText = view.findViewById(R.id.emailEditText)
        passwordEditText = view.findViewById(R.id.passwordEditText)
        loginTosignUp = view.findViewById(R.id.loginTosignUp)
        adminLoginBtn = view.findViewById(R.id.adminLoginBtn)
        // val loginButton = view.findViewById<Button>(R.id.loginButton) // not needed when using binding


        builder = AlertDialog.Builder(requireContext(), R.style.MyAlertDialog)

        progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Logging In")
        progressDialog.setMessage("Please wait...")
        progressDialog.setCancelable(false)

        auth = FirebaseAuth.getInstance()

        binding.loginButton.setOnClickListener {

            //TODO: Delete this and implement login code which I commented out for easy login
            //navigateToProfilePage()



            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {

                signIn(email, password)

            } else {
                builder.setTitle("Please enter both your email and password to continue.")
                builder.setPositiveButton("OK", null)
                builder.show()
            }





        }

        // If Sign up button clicked
        loginTosignUp.setOnClickListener{
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }


    }


    // method to sign in using email and password and navigate to profile page
    private fun signIn(email: String, password: String) {
        progressDialog.show()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    progressDialog.dismiss()
                    navigateToProfilePage()
                } else {
                    progressDialog.dismiss()
                    val exception = task.exception

                    // Got this from chatgpt- to understand what kind of exception it if like if user doesn't exist or password is wrong etc

                    if (exception is FirebaseAuthInvalidUserException) {
                        // Handle the case where the user account does not exist.
                        builder.setTitle("User Authentication failed..")
                        builder.setMessage("The email address is not registered.")
                    } else if (exception is FirebaseAuthInvalidCredentialsException) {
                        // Handle the case where the credentials are invalid.
                        builder.setTitle("User Authentication failed..")
                        builder.setMessage("The email or password is incorrect.")
                    } else {
                        // Handle other exceptions.
                        builder.setTitle("User Authentication failed..")
                        builder.setMessage("There was an error in login. Please try again.")
                    }
                    builder.setPositiveButton("OK", null)
                    builder.show()
                    Toast.makeText(requireContext(), "User Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }




    private fun navigateToProfilePage() {

        // NEW nav controller method to navigate
        findNavController().navigate(R.id.action_FirstFragment_to_HomeActivity)


        // OLD NAVIGATE METHOD USING INTENT

        // Create an Intent object
        //val intent = Intent(requireContext(), HomeActivity::class.java)
        // Start the WelcomeFragment
        //startActivity(intent)



    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}