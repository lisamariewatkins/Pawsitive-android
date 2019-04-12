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
import com.example.petmatcher.DI.Injectable
import com.example.petmatcher.R
import javax.inject.Inject


/**
 * @author Lisa Watkins
 *
 * Displays list of shelters in a user's area
 */
class OrganizationSearchFragment : Fragment(), Injectable {
    private lateinit var shelterRecyclerView: RecyclerView
    private lateinit var organizationListAdapter: PagedListAdapter<Organization, OrganizationListAdapter.OrganizationViewHolder>
    private lateinit var shelterLayoutManager: RecyclerView.LayoutManager

    private lateinit var pullToRefresh: SwipeRefreshLayout

    private lateinit var viewModel: OrganizationSearchViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        viewModel = ViewModelProviders.of(this, viewModelFactory)[OrganizationSearchViewModel::class.java]

        pullToRefresh = view.findViewById(R.id.shelter_refresh)
        pullToRefresh.setOnRefreshListener {
            viewModel.refresh()
            pullToRefresh.isRefreshing = false
        }

        shelterLayoutManager = LinearLayoutManager(context)
        organizationListAdapter = OrganizationListAdapter()
        shelterRecyclerView = view.findViewById<RecyclerView>(R.id.shelter_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = shelterLayoutManager
            adapter = organizationListAdapter
        }

        viewModel.organizationsList.observe(viewLifecycleOwner, Observer {
            organizationListAdapter.submitList(it)
        })

        return view
    }

}
