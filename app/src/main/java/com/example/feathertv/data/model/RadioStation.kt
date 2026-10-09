package com.example.feathertv.data.model

data class RadioStation(
    val id: Int,
    val name: String,
    val slogan: String,
    val location: String,
    val streamUrl: String,
    val colorHex: String,
    val logoUrl: String,
    val category: String,
    val isPopular: Boolean = false,
    val isTrending: Boolean = false
)
