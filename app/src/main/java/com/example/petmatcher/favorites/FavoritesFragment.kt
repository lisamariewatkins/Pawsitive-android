package com.example.petmatcher.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.petmatcher.DI.Injectable
import com.example.petmatcher.R
import com.example.petmatcher.data.Favorite
import javax.inject.Inject


/**
 * @author Lisa Watkins
 */
class FavoritesFragment: Fragment(), Injectable {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: ListAdapter<Favorite, FavoritesListAdapter.FavoritesViewHolder>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private lateinit var viewModel: FavoritesViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favorites, container, false)

        viewModel = ViewModelProviders.of(this, viewModelFactory)[FavoritesViewModel::class.java]

        viewManager = LinearLayoutManager(context)
        viewAdapter = FavoritesListAdapter()
        recyclerView = view.findViewById<RecyclerView>(R.id.favorites_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        viewModel.favorites.observe(this, Observer<List<Favorite>> {
            viewAdapter.submitList(it)
        })

        return view
    }

}
