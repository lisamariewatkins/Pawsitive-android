package com.example.petmatcher.petdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.petmatcher.DI.Injectable
import com.example.petmatcher.R
import com.example.petmatcher.favorites.FavoritesListAdapter.Companion.PET_ID_KEY
import java.lang.IllegalArgumentException
import javax.inject.Inject


class DetailsFragment: Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: DetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_details, container, false)
        val petImageView = view.findViewById<ImageView>(R.id.pet_image)
        val petName = view.findViewById<TextView>(R.id.pet_name)
        val petDescription = view.findViewById<TextView>(R.id.pet_description)
        val petId = arguments?.getInt(PET_ID_KEY) ?: throw IllegalArgumentException("Pet ID must not be null.")

        viewModel = ViewModelProviders.of(this, viewModelFactory)[DetailsViewModel::class.java]

        viewModel.errorState.observe(viewLifecycleOwner, Observer {
            // TODO: Come up with an error state if pet isn't in local db
        })

        viewModel.getPet(petId).observe(viewLifecycleOwner, Observer {favorite ->
            viewModel.loadPet(
                petImageView = petImageView,
                petNameTextView = petName,
                petDescriptionTextView = petDescription,
                favorite = favorite)
        })

        return view
    }
}
