package com.dilaefendioglu.artify.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dilaefendioglu.artify.data.response.Image
import com.dilaefendioglu.artify.databinding.ItemImageBinding
import com.dilaefendioglu.artify.utils.setImageUrl

class ImageAdapter(
    private var images: List<Image>,
    private val onItemClick: (Image) -> Unit
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = images[position]
        holder.bind(image)
        holder.itemView.setOnClickListener { onItemClick(image) }
    }

    override fun getItemCount(): Int = images.size

    fun updateData(newImages: List<Image>) {
        images = newImages
        notifyDataSetChanged()
    }

    class ImageViewHolder(private val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(image: Image) {
            binding.imgArt.setImageUrl(image.src)
        }
    }
}