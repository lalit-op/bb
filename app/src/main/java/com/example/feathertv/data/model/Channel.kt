package com.example.feathertv.data.model

data class Channel(
    val id: String,
    val name: String,
    val category: String,
    val logoUrl: String,
    val streamUrl: String = "",
    val webUrl: String = ""
)
