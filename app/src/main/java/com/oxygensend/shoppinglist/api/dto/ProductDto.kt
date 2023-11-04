package com.oxygensend.shoppinglist.api.dto

data class ProductDto(
    val name: String,
    val quantity: Int = 1,
    val grammar: String = "PIECE"
)
