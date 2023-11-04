package com.oxygensend.shoppinglist.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import com.oxygensend.shoppinglist.api.ShoppingListClient
import com.oxygensend.shoppinglist.api.dto.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShoppingListsViewModel : ViewModel() {
    private val _shoppingLists = MutableStateFlow<List<ShoppingListDto>>(emptyList())
    val shoppingLists: StateFlow<List<ShoppingListDto>> get() = _shoppingLists


    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> get() = _message

    private val _closeShoppingListCreate = MutableStateFlow<Boolean>(false)
    val closeShoppingListCreate: StateFlow<Boolean> get() = _closeShoppingListCreate

    private val _selectedShoppingList = MutableStateFlow<ShoppingListGet?>(null)
    val selectedShoppingList: StateFlow<ShoppingListGet?> get() = _selectedShoppingList

    fun closeShoppingListCreate() {
        _closeShoppingListCreate.value = true
    }


    fun showMessage(message: String) {
        _message.value = message
    }

    fun removeMessage() {
        _message.value = null
    }

    fun removeShoppingList(shoppingList: ShoppingListDto) {
        ShoppingListClient.deleteShoppingList(shoppingList.id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    val currentShoppingLists = _shoppingLists.value.toMutableList()
                    currentShoppingLists.remove(shoppingList)
                    _shoppingLists.value = currentShoppingLists

                    showMessage("Shopping list '${shoppingList.name}' deleted")
                }
                Log.d("ShoppingList", response.toString())
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d("ShoppingList", t.toString());
            }
        })
    }

    fun fetchShoppingLists() {
        ShoppingListClient.shoppingLists()
            .enqueue(object : Callback<ShoppingListResponse> {
                override fun onResponse(
                    call: Call<ShoppingListResponse>,
                    response: Response<ShoppingListResponse>
                ) {
                    if (response.isSuccessful) {
                        val shoppingLists = response.body()?.data ?: emptyList()
                        _shoppingLists.value = shoppingLists
                    }
                }

                override fun onFailure(call: Call<ShoppingListResponse>, t: Throwable) {
                    Log.e("ShoppingList", t.toString())
                }
            })
    }

    fun updateShoppingList(shoppingList: ShoppingListDto, completed: Boolean?) {
        val request = UpdateShoppingListRequest(
            completed = completed
        )
        ShoppingListClient.updateShoppingList(shoppingList.id, request)
            .enqueue(object : Callback<ShoppingListDto> {
                override fun onResponse(
                    call: Call<ShoppingListDto>,
                    response: Response<ShoppingListDto>
                ) {
                    if (response.isSuccessful) {
                        val updatedShoppingList = response.body()!!
                        val currentShoppingLists = _shoppingLists.value.toMutableList()
                        val index = currentShoppingLists.indexOfFirst { it.id == updatedShoppingList.id }
                        currentShoppingLists[index] = updatedShoppingList
                        _shoppingLists.value = currentShoppingLists
                    }
                }

                override fun onFailure(call: Call<ShoppingListDto>, t: Throwable) {
                    Log.e("ShoppingList", t.toString())
                }
            })
    }

    fun createShoppingList(request: CreateShoppingListRequest) {
        ShoppingListClient.createShoppingList(request)
            .enqueue(object : Callback<ShoppingListDto> {
                override fun onResponse(
                    call: Call<ShoppingListDto>,
                    response: Response<ShoppingListDto>
                ) {
                    if (response.isSuccessful) {
                        val shoppingList = response.body()!!
                        val currentShoppingLists = _shoppingLists.value.toMutableList()
                        currentShoppingLists.add(shoppingList)
                        _shoppingLists.value = currentShoppingLists

                        closeShoppingListCreate()

                    }

                    if (response.code() == 400) {
                        showMessage("Shopping list name cannot be empty")
                        Log.d("ShoppingList", response.toString())
                    }
                }

                override fun onFailure(call: Call<ShoppingListDto>, t: Throwable) {
                    Log.d("ShoppingList", t.toString())
                    showMessage("GOWNO NIE DZIALA")
                }
            })
    }

    fun getShoppingListById(shoppingListId: String) {
        ShoppingListClient.getShoppingList(shoppingListId)
            .enqueue(object : Callback<ShoppingListGet> {
                override fun onResponse(
                    call: Call<ShoppingListGet>,
                    response: Response<ShoppingListGet>
                ) {
                    if (response.isSuccessful) {
                        val shoppingList = response.body()!!
                        _selectedShoppingList.value = shoppingList
                    }
                }

                override fun onFailure(call: Call<ShoppingListGet>, t: Throwable) {
                    Log.e("ShoppingList", t.toString())
                }
            });

    }
}