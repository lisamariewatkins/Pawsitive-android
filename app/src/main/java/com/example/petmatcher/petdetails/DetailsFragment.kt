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
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.petmatcher.DI.Injectable
import com.example.petmatcher.R
import com.example.petmatcher.data.Favorite
import javax.inject.Inject


class DetailsFragment : Fragment(), Injectable {

    private val args: DetailsFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: DetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_details, container, false)
        val petImage = view.findViewById<ImageView>(R.id.pet_image)
        val petName = view.findViewById<TextView>(R.id.pet_name)
        val petDescription = view.findViewById<TextView>(R.id.pet_description)
        val petId = args.PetId

        viewModel = ViewModelProviders.of(this, viewModelFactory)[DetailsViewModel::class.java]

        viewModel.getPet(petId).observe(this, Observer {favorite ->
            petName.text = favorite.name
            petDescription.text = favorite.description

            Glide.with(petImage.context)
                .load(favorite.imageUrl)
                .into(petImage)
        })

        return view
    }
}
