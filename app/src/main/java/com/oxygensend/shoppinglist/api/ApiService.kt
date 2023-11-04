package com.oxygensend.shoppinglist.api

import com.oxygensend.shoppinglist.api.dto.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST(API.LOGIN_URL)
    fun login(@Body loginRequest: LoginRequest): Call<AuthResponse>

    @GET(API.SHOPPING_LISTS_URL)
    fun getShoppingLists(): Call<ShoppingListResponse>

    @DELETE(API.SHOPPING_LIST_ID_URL)
    fun deleteShoppingList(@Path("id") shoppingListId: String): Call<Void>

    @Multipart
    @PATCH(API.SHOPPING_LIST_ID_URL)
    fun updateShoppingList(
        @Path("id") shoppingListId: String,
        @Part("request") shoppingList: UpdateShoppingListRequest
    ): Call<ShoppingListDto>

    @Multipart
    @POST(API.SHOPPING_LISTS_URL)
    fun createShoppingList(
        @Part("request") shoppingList: CreateShoppingListRequest
    ): Call<ShoppingListDto>
}
