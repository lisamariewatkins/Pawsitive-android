package com.example.petmatcher.home

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import com.example.petmatcher.R

/**
 * Home screen of the app and start fragment in the navigation graph. Hosts the pet cards that a user can swipe.
 */
class HomeFragment : Fragment() {
    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val petCard: CardView = view.findViewById(R.id.pet_card)

        petCard.setOnClickListener {
            it.findNavController().navigate(R.id.detailsFragment)
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}
