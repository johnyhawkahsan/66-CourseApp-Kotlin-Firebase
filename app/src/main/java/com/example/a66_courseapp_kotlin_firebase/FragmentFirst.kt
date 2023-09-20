package com.example.a66_courseapp_kotlin_firebase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.a66_courseapp_kotlin_firebase.databinding.FragmentFirstBinding

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


    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

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
        val loginButton = view.findViewById<Button>(R.id.loginButton)



        loginTosignUp.setOnClickListener{
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}