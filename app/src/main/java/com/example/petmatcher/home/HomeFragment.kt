package com.example.petmatcher.home

import android.os.Bundle
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
import com.example.network.petlist.Pet
import com.example.petmatcher.memorycache.ImageCache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

/**
 * Home screen of the app and start fragment in the navigation graph. Hosts the pet cards that a user can swipe.
 */
class HomeFragment: Fragment(), Injectable, CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

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
            viewModel.showPet(pet, petNameTextView, petDescriptionTextView, petImage)
        })

        petImage.setOnClickListener {
            viewModel.nextPet()
        }

        matchButton.setOnClickListener {
            viewModel.addCurrentPetToFavorites()
        }

        return view
    }
}
