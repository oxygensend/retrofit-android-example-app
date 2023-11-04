package com.oxygensend.shoppinglist.api

import com.oxygensend.shoppinglist.api.dto.*
import retrofit2.Call

class ApiClient {
    companion object {
        fun login(request: LoginRequest): Call<AuthResponse> {
            return RetrofitClient.apiService.login(request)
        }

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
    }
}