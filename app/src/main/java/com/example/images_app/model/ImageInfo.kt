package com.example.images_app.model

data class ImageInfo(
    val id: String,
    val author: String,
    val width: Int,
    val height: Int,
    val url: String,
    val downloadUrl: String
)
