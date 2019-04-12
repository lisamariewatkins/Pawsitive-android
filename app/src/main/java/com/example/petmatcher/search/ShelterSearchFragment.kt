package com.example.petmatcher.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.network.organizations.Organization
import com.example.network.shelter.Shelter
import com.example.petmatcher.DI.Injectable
import com.example.petmatcher.R
import javax.inject.Inject


/**
 * @author Lisa Watkins
 *
 * Displays list of shelters in a user's area
 */
class ShelterSearchFragment : Fragment(), Injectable {
    private lateinit var shelterRecyclerView: RecyclerView
    private lateinit var shelterListAdapter: PagedListAdapter<Organization, ShelterListAdapter.ShelterViewHolder>
    private lateinit var shelterLayoutManager: RecyclerView.LayoutManager

    private lateinit var pullToRefresh: SwipeRefreshLayout

    private lateinit var viewModel: ShelterSearchViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        viewModel = ViewModelProviders.of(this, viewModelFactory)[ShelterSearchViewModel::class.java]

        pullToRefresh = view.findViewById(R.id.shelter_refresh)
        pullToRefresh.setOnRefreshListener {
            viewModel.refresh()
            pullToRefresh.isRefreshing = false
        }

        shelterLayoutManager = LinearLayoutManager(context)
        shelterListAdapter = ShelterListAdapter()
        shelterRecyclerView = view.findViewById<RecyclerView>(R.id.shelter_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = shelterLayoutManager
            adapter = shelterListAdapter
        }

        viewModel.sheltersList.observe(viewLifecycleOwner, Observer {
            shelterListAdapter.submitList(it)
        })

        return view
    }

}
