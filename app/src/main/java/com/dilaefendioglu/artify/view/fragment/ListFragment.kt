package com.dilaefendioglu.artify.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.dilaefendioglu.artify.adapter.ImageAdapter
import com.dilaefendioglu.artify.databinding.FragmentListBinding
import com.dilaefendioglu.artify.utils.Constants
import com.dilaefendioglu.artify.utils.hideKeyboard
import com.dilaefendioglu.artify.viewmodel.MainViewModel

class ListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ImageAdapter(emptyList()) { image ->
            val action = ListFragmentDirections.actionListFragmentToDetailFragment(image.src)
            findNavController().navigate(action)
        }

        binding.recyclerViewImage.layoutManager =
            GridLayoutManager(requireContext(), Constants.GRID_SPAN_COUNT)
        binding.recyclerViewImage.adapter = adapter
        binding.loadingGif.isVisible = viewModel.isGifVisible


        viewModel.images.observe(viewLifecycleOwner) { images ->
            if (images != null) {
                adapter.updateData(images)
            }
        }

        binding.generateButton.setOnClickListener {
            val query = binding.searchEditText.text.toString().trim()
            if (query.isNotEmpty()) {
                hideKeyboard()
                binding.loadingGif.isVisible = false
                viewModel.isGifVisible = false
                viewModel.fetchImages(query)
            }
        }
    }
}