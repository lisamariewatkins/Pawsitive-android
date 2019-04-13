package com.example.petmatcher.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
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
        val petInfo: ScrollView = view.findViewById(R.id.pet_info)
        val progressBar: ProgressBar = view.findViewById(R.id.pet_progress_bar)
        val petImage: ImageView = view.findViewById(R.id.pet_image)
        val petNameTextView: TextView = view.findViewById(R.id.pet_name)
        val petDescriptionTextView: TextView = view.findViewById(R.id.pet_description)
        val matchButton: Button = view.findViewById(R.id.match_button)

        viewModel = ViewModelProviders.of(this, viewModelFactory)[HomeViewModel::class.java]

        viewModel.currentAnimal.observe(viewLifecycleOwner, Observer { pet ->
            viewModel.showAnimal(pet, petNameTextView, petDescriptionTextView, petImage)
        })

        viewModel.networkState.observe(viewLifecycleOwner, Observer { networkState ->
            viewModel.displayViewByNetworkState(petLayout = petInfo, progressBar = progressBar, state = networkState)
        })

        petImage.setOnClickListener {
            viewModel.nextAnimal()
        }

        matchButton.setOnClickListener {
            viewModel.addCurrentAnimalToFavorites()
        }

        return view
    }


}
