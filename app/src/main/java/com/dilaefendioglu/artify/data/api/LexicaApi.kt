package com.dilaefendioglu.artify.data.api

import com.dilaefendioglu.artify.data.service.LexicaService
import com.dilaefendioglu.artify.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object LexicaApi {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val api: LexicaService by lazy {
        retrofit.create(LexicaService::class.java)
    }
}
