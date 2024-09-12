package com.dilaefendioglu.artify.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.dilaefendioglu.artify.adapter.FavoriteImageAdapter
import com.dilaefendioglu.artify.databinding.FragmentFavoriteBinding
import com.dilaefendioglu.artify.utils.Constants
import com.dilaefendioglu.artify.viewmodel.DetailViewModel

class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var viewModel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)

        val adapter = FavoriteImageAdapter(
            emptyList(),
            onItemClicked = { favoriteImage ->
                val action =
                    FavoriteFragmentDirections.actionFavoritesFragmentToDetailFragment(favoriteImage.imageUrl)
                findNavController().navigate(action)
            }
        )

        binding.recyclerViewFavorites.layoutManager =
            GridLayoutManager(requireContext(), Constants.GRID_SPAN_COUNT)
        binding.recyclerViewFavorites.adapter = adapter

        viewModel.getFavorites().observe(viewLifecycleOwner) { favorites ->
            if (favorites.isNotEmpty()) {
                binding.loadingAnimation.visibility = View.GONE
                binding.recyclerViewFavorites.visibility = View.VISIBLE
                adapter.updateData(favorites)
            } else {
                binding.loadingAnimation.visibility = View.VISIBLE
                binding.recyclerViewFavorites.visibility = View.GONE
            }
        }
    }
}