package com.dilaefendioglu.artify.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.dilaefendioglu.artify.database.AppDatabase
import com.dilaefendioglu.artify.database.FavoriteImage
import com.google.firebase.auth.FirebaseAuth

class DetailViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).favoriteImageDao()

    fun getFavorites(): LiveData<List<FavoriteImage>> {
        val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: ""
        return dao.getFavoritesByEmail(userEmail)
    }

    suspend fun addToFavorites(imageUrl: String) {
        val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: return
        val favoriteImage = FavoriteImage(imageUrl = imageUrl, userEmail = userEmail)
        dao.insert(favoriteImage)
    }

    suspend fun removeFromFavorites(imageUrl: String) {
        val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: return
        dao.deleteByImageUrlAndEmail(imageUrl, userEmail)
    }
}