package com.dilaefendioglu.artify.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_images")
data class FavoriteImage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val imageUrl: String,
    val userEmail: String
)
