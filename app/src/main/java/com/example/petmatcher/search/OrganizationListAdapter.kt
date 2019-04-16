package com.example.petmatcher.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.petmatcher.R
import com.example.petmatcher.data.api.organizations.Organization
import kotlinx.android.synthetic.main.shelter_list_item.view.*

class OrganizationListAdapter: PagedListAdapter<Organization, OrganizationListAdapter.OrganizationViewHolder>(OrganizationDiffCallback()) {

    class OrganizationViewHolder(val view: ConstraintLayout): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrganizationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.shelter_list_item, parent, false) as ConstraintLayout

        return OrganizationViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrganizationViewHolder, position: Int) {
        holder.view.shelter_name.text = getItem(position)?.name
    }
}

/**
 * Called when data is updated on a background thread. Checks the new data against existing data to decide whether
 * or not to add new items.
 */
class OrganizationDiffCallback : DiffUtil.ItemCallback<Organization>() {
    override fun areItemsTheSame(oldItem: Organization, newItem: Organization): Boolean {
        return oldItem.id == newItem.id
    }

    /**
     * Asks whether the content of the same item is the same. Check to ensure that none of the displayed
     * content has changed.
     */
    override fun areContentsTheSame(oldItem: Organization, newItem: Organization): Boolean {
        return oldItem.name == newItem.name
    }
}