package com.example.petmatcher.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.petmatcher.R
import com.example.petmatcher.data.Favorite
import kotlinx.android.synthetic.main.favorites_list_item.view.*

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