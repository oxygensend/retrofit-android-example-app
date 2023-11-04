package com.oxygensend.shoppinglist.api.dto

data class ShoppingListGet(val id: String, val name: String, val completed: Boolean, val products: List<ListElement>)
