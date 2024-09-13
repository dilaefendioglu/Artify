package com.dilaefendioglu.artify.data.response

data class ImageResponse(
    val images: List<Image>
)

data class Image(
    val src: String
)

