package com.example.petmatcher.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.petmatcher.R
import com.example.petmatcher.data.Favorite
import kotlinx.android.synthetic.main.favorites_list_item.view.*

/**
 * @author Lisa Watkins
 *
 * ListAdapter that runs DiffUtil on a background thread to update the list with changes.
 */
class FavoritesListAdapter: ListAdapter<Favorite, FavoritesListAdapter.FavoritesViewHolder>(FavoriteDiffCallback()) {

    class FavoritesViewHolder(val view: ConstraintLayout): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.favorites_list_item, parent, false) as ConstraintLayout

        return FavoritesViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        holder.view.favorite_name.text = getItem(position).name
        holder.view.favorite_breed.text = getItem(position).breed

        val petId = getItem(position).petId

        val bundle = bundleOf(PET_ID_KEY to petId)

        holder.view.setOnClickListener {
            it.findNavController().navigate(R.id.action_favoritesFragment_to_detailsFragment,
                bundle)
        }
    }

    companion object {
        const val PET_ID_KEY = "petId"
    }
}

class FavoriteDiffCallback : DiffUtil.ItemCallback<Favorite>() {
    override fun areItemsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
        return oldItem.petId == newItem.petId
    }

    override fun areContentsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
        return oldItem == newItem
    }
}