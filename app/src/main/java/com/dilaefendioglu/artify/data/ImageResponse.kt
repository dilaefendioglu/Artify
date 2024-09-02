package com.dilaefendioglu.artify.data

data class ImageResponse (
    val images: List<Image>
)

data class Image(
    val src: String
)

