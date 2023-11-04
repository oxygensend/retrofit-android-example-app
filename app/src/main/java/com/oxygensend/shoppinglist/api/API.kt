package com.oxygensend.shoppinglist.api

class API {
    companion object {
        const val BASE_URL = "http://192.168.0.234:8080/api/v1/"
        const val LOGIN_URL = "auth/access_token"
        const val SHOPPING_LISTS_URL = "shopping-lists"
        const val SHOPPING_LIST_ID_URL = "shopping-lists/{id}"
    }
}