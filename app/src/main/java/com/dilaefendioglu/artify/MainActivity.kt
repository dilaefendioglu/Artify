package com.dilaefendioglu.artify

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dilaefendioglu.artify.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        viewModel.isLoading.observe(this) { isLoading ->
            binding.txtloading.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.hasError.observe(this) { hasError ->
            binding.txtError.visibility = if (hasError) View.VISIBLE else View.GONE
        }
    }
}