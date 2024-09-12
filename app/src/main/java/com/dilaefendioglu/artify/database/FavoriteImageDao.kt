package com.dilaefendioglu.artify.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoriteImageDao {
    @Insert
    suspend fun insert(favoriteImage: FavoriteImage)

    @Query("SELECT * FROM favorite_images WHERE userEmail = :userEmail")
    fun getFavoritesByEmail(userEmail: String): LiveData<List<FavoriteImage>>

    @Query("DELETE FROM favorite_images WHERE imageUrl = :imageUrl AND userEmail = :userEmail")
    suspend fun deleteByImageUrlAndEmail(imageUrl: String, userEmail: String)
}