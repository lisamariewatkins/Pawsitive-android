package com.example.petmatcher.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.network.petlist.Pet
import com.example.petmatcher.R
import kotlinx.android.synthetic.main.favorites_list_item.view.*

class FavoritesListAdapter(private val data: List<Pet>): RecyclerView.Adapter<FavoritesListAdapter.FavoritesViewHolder>() {

    class FavoritesViewHolder(val view: ConstraintLayout): RecyclerView.ViewHolder(view)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.favorites_list_item, parent, false) as ConstraintLayout
        return FavoritesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        holder.view.favorite_name.text = data[position].name.value
        holder.view.favorite_breed.text = data[position].animal.value
    }
}