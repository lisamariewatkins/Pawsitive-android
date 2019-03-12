package com.example.petmatcher.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.petmatcher.DI.Injectable
import com.example.petmatcher.R
import javax.inject.Inject
import com.bumptech.glide.request.RequestOptions
import com.example.petmatcher.PetRepository
import com.example.petmatcher.favorites.FavoritesRepository

/**
 * Home screen of the app and start fragment in the navigation graph. Hosts the pet cards that a user can swipe.
 */
class HomeFragment: Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var favoritesRepository: FavoritesRepository

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val petImage: ImageView = view.findViewById(R.id.pet_image)
        val petNameTextView: TextView = view.findViewById(R.id.pet_name)
        val petDescriptionTextView: TextView = view.findViewById(R.id.pet_description)
        val matchButton: Button = view.findViewById(R.id.match_button)

        viewModel = ViewModelProviders.of(this, viewModelFactory)[HomeViewModel::class.java]

        viewModel.currentPet.observe(viewLifecycleOwner, Observer { pet ->
            val petName = pet?.name?.value
            val petDescription = pet?.description?.value
            val imageUrl = pet?.media?.photos?.photoList?.get(3)?.url
            petNameTextView.text = petName
            petDescriptionTextView.text = petDescription
            loadImage(petImage, imageUrl)
        })

        petImage.setOnClickListener {
            viewModel.nextPet()
        }

        matchButton.setOnClickListener {
            viewModel.currentPet.value?.let {
                favoritesRepository.addToFavorites(it)
            }
        }

        return view
    }

    // todo move this
    private fun loadImage(petImage: ImageView, imageUrl: String?) {
        val options = RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.ic_placeholder_image) // todo find a placeholder asset
            .error(R.drawable.ic_placeholder_image)

        Glide.with(petImage.context).load(imageUrl).apply(options).into(petImage)
    }
}
