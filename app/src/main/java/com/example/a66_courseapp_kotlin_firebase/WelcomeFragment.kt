package com.example.a66_courseapp_kotlin_firebase

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/** A simple [Fragment] subclass. Use the [WelcomeFragment.newInstance] factory method to create an instance of this fragment. */
class WelcomeFragment : Fragment() {


    private lateinit var profileImageView: ImageView
    private lateinit var welcomeToProfileTextView: TextView

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
        profileImageView = view.findViewById(R.id.profileImageView)
        welcomeToProfileTextView = view.findViewById(R.id.welcometoprofile)
        welcomeToProfileTextView.setText(param1)

        return view
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