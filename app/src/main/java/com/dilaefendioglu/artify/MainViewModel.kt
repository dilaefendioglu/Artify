package com.dilaefendioglu.artify

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dilaefendioglu.artify.api.LexicaApi
import com.dilaefendioglu.artify.data.Image
import com.dilaefendioglu.artify.data.ImageResponse
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    var isGifVisible = true

    private val lexicaService = LexicaApi.api

    private val _images = MutableLiveData<List<Image>>()
    val images: LiveData<List<Image>> get() = _images

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _hasError = MutableLiveData<Boolean>()
    val hasError: LiveData<Boolean> get() = _hasError

    fun fetchImages(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = lexicaService.getImages(query)
                if (response.isSuccessful && response.body() != null) {
                    val imageResponses = response.body()!!.images
                    _images.value = imageResponses
                    _hasError.value = false
                } else {
                    _hasError.value = true
                }

            } catch (e: Exception) {
                _hasError.value = true
            } finally {
                _isLoading.value = false
            }
        }
    }
}