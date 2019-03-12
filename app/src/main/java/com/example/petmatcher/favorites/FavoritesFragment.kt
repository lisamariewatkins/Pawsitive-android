package com.example.petmatcher.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petmatcher.DI.Injectable
import com.example.petmatcher.R
import javax.inject.Inject


/**
 * @author Lisa Watkins
 */
class FavoritesFragment: Fragment(), Injectable {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    @Inject
    lateinit var favoritesRepository: FavoritesRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favorites, container, false)

        viewManager = LinearLayoutManager(context)
        viewAdapter = FavoritesListAdapter(favoritesRepository.getFavoritesList())

        recyclerView = view.findViewById<RecyclerView>(R.id.favorites_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        return view
    }

}
