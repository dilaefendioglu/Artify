package com.dilaefendioglu.artify.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dilaefendioglu.artify.database.FavoriteImage
import com.dilaefendioglu.artify.databinding.ItemFavoriteBinding
import com.dilaefendioglu.artify.utils.setImageUrl

class FavoriteImageAdapter(
    private var favorites: List<FavoriteImage>,
    private val onItemClicked: (FavoriteImage) -> Unit
) : RecyclerView.Adapter<FavoriteImageAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(favoriteImage: FavoriteImage) {
            binding.imgArt.setImageUrl(favoriteImage.imageUrl)

            binding.root.setOnClickListener {
                onItemClicked(favoriteImage)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(favorites[position])
    }

    override fun getItemCount(): Int = favorites.size

    fun updateData(newFavorites: List<FavoriteImage>) {
        favorites = newFavorites
        notifyDataSetChanged()
    }
}
