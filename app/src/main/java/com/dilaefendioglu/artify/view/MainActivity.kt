package com.dilaefendioglu.artify.view

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dilaefendioglu.artify.R
import com.dilaefendioglu.artify.databinding.ActivityMainBinding
import com.dilaefendioglu.artify.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Artify);
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(this) { isLoading ->
            binding.txtLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.hasError.observe(this) { hasError ->
            binding.txtError.visibility = if (hasError) View.VISIBLE else View.GONE
        }
    }
}