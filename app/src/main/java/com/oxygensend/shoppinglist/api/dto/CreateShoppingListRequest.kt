package com.oxygensend.shoppinglist.api.dto


data class CreateShoppingListRequest(
    val name: String,
    val products: List<ProductDto>
)

