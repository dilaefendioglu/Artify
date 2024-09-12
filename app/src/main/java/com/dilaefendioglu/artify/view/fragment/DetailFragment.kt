package com.dilaefendioglu.artify.view.fragment

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dilaefendioglu.artify.R
import com.dilaefendioglu.artify.databinding.FragmentDetailBinding
import com.dilaefendioglu.artify.utils.Constants
import com.dilaefendioglu.artify.utils.setImageUrl
import com.dilaefendioglu.artify.viewmodel.DetailViewModel
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import java.io.OutputStream

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val args: DetailFragmentArgs by navArgs()
    private val viewModel: DetailViewModel by viewModels()
    private var isFavorite = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imgArt.setImageUrl(args.src)
        checkIfFavorite(args.src)
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.downloadButton.setOnClickListener {
            saveImageToGallery(args.src)
        }

        binding.cancelButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.favoriteButton.setOnClickListener {
            if (isFavorite) {
                removeFromFavorites(args.src)
            } else {
                addToFavorites(args.src)
            }
            updateFavoriteStatus()
        }
    }

    private fun checkIfFavorite(imageUrl: String) {
        viewModel.getFavorites().observe(viewLifecycleOwner) { favorites ->
            isFavorite = favorites.any { it.imageUrl == imageUrl }
            toggleFavoriteIcon()
        }
    }

    private fun addToFavorites(imageUrl: String) {
        lifecycleScope.launch {
            viewModel.addToFavorites(imageUrl)
        }
    }

    private fun removeFromFavorites(imageUrl: String) {
        lifecycleScope.launch {
            viewModel.removeFromFavorites(imageUrl)
        }
    }

    private fun updateFavoriteStatus() {
        checkIfFavorite(args.src)
    }

    private fun toggleFavoriteIcon() {
        val favoriteIcon = if (isFavorite) {
            R.drawable.heart
        } else {
            R.drawable.like
        }
        binding.favoriteButton.setImageResource(favoriteIcon)
    }

    private fun saveImageToGallery(imageUrl: String) {
        Picasso.get().load(imageUrl).into(object : com.squareup.picasso.Target {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                bitmap?.let { saveBitmapToGallery(it) } ?: showToast("Failed to load image")
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                showToast("Failed to load image")
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
        })
    }

    private fun saveBitmapToGallery(bitmap: Bitmap) {
        val contentValues = ContentValues().apply {
            put(
                MediaStore.Images.Media.DISPLAY_NAME,
                "Downloaded_Image_${System.currentTimeMillis()}.jpg"
            )
            put(MediaStore.Images.Media.MIME_TYPE, Constants.IMAGE_MIME_TYPE)
            put(MediaStore.Images.Media.RELATIVE_PATH,Constants.IMAGE_RELATIVE_PATH)
        }

        val uri = requireContext().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )

        uri?.let { imageUri ->
            requireContext().contentResolver.openOutputStream(imageUri)
                ?.use { outputStream: OutputStream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    showToast("Image saved to gallery")
                } ?: showToast("Failed to open output stream")
        } ?: showToast("Failed to create image URI")
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
