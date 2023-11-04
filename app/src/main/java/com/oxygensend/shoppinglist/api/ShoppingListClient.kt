package com.oxygensend.shoppinglist.api

import com.oxygensend.shoppinglist.api.dto.*
import retrofit2.Call

class ShoppingListClient {
    companion object {
        fun shoppingLists(): Call<ShoppingListResponse> {
            return RetrofitClient.authenticatedService.getShoppingLists()
        }

        fun deleteShoppingList(shoppingListId: String): Call<Void> {
            return RetrofitClient.authenticatedService.deleteShoppingList(shoppingListId)
        }

        fun updateShoppingList(shoppingListId: String, shoppingList: UpdateShoppingListRequest): Call<ShoppingListDto> {
            return RetrofitClient.authenticatedService.updateShoppingList(shoppingListId, shoppingList)
        }

        fun createShoppingList(shoppingList: CreateShoppingListRequest): Call<ShoppingListDto> {
            return RetrofitClient.authenticatedService.createShoppingList(shoppingList)
        }

        fun getShoppingList(shoppingListId: String): Call<ShoppingListGet> {
            return RetrofitClient.authenticatedService.getShoppingList(shoppingListId)
        }
    }
}