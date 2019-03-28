package com.example.petmatcher.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.network.shelter.Shelter
import com.example.petmatcher.R
import kotlinx.android.synthetic.main.shelter_list_item.view.*

class ShelterListAdapter: ListAdapter<Shelter, ShelterListAdapter.ShelterViewHolder>(ShelterDiffCallback()) {

    class ShelterViewHolder(val view: ConstraintLayout): RecyclerView.ViewHolder(view)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShelterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.favorites_list_item, parent, false) as ConstraintLayout

        return ShelterViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShelterViewHolder, position: Int) {
        holder.view.shelter_name.text = getItem(position).name.value
        holder.view.shelter_city.text = getItem(position).city.value
    }
}

class ShelterDiffCallback : DiffUtil.ItemCallback<Shelter>() {
    override fun areItemsTheSame(oldItem: Shelter, newItem: Shelter): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Shelter, newItem: Shelter): Boolean {
        return oldItem == newItem
    }
}