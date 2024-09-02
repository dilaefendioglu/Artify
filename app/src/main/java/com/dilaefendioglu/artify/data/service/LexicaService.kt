package com.dilaefendioglu.artify.data.service

import com.dilaefendioglu.artify.data.response.ImageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LexicaService {
    @GET("api/v1/search")
    suspend fun getImages(@Query("q") query: String): Response<ImageResponse>
}
