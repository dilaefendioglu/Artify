package com.dilaefendioglu.artify.api

import com.dilaefendioglu.artify.service.LexicaService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object LexicaApi {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://lexica.art/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val api: LexicaService by lazy {
        retrofit.create(LexicaService::class.java)
    }
}
