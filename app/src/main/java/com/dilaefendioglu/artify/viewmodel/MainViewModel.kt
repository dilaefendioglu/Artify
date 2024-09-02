package com.dilaefendioglu.artify.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dilaefendioglu.artify.data.api.LexicaApi
import com.dilaefendioglu.artify.data.response.Image
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
                if (response.isSuccessful) {
                    response.body()?.let {
                        val imageResponses = it.images
                        _images.value = imageResponses
                        _hasError.value = false
                    } ?: run {
                        _hasError.value = true
                    }
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