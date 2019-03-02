package com.example.petmatcher.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.petmatcher.DI.Injectable
import com.example.petmatcher.R
import javax.inject.Inject

/**
 * Home screen of the app and start fragment in the navigation graph. Hosts the pet cards that a user can swipe.
 */
class HomeFragment: Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val petCard: CardView = view.findViewById(R.id.pet_card)

        viewModel = ViewModelProviders.of(this, viewModelFactory)[HomeViewModel::class.java]

        petCard.setOnClickListener {
            // it.findNavController().navigate(R.id.detailsFragment)
            val petName = viewModel.getPet().value?.name?.value
            Toast.makeText(context, petName ?: "name null", Toast.LENGTH_LONG).show()
        }

        return view
    }
}
