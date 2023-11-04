package com.oxygensend.shoppinglist.api

import com.oxygensend.shoppinglist.api.dto.AuthResponse
import com.oxygensend.shoppinglist.api.dto.LoginRequest
import com.oxygensend.shoppinglist.api.dto.RefreshTokenRequest
import retrofit2.Call

class AuthClient {

    companion object {

        fun login(request: LoginRequest): Call<AuthResponse> {
            return RetrofitClient.apiService.login(request)
        }

        fun refreshToken(request: RefreshTokenRequest): Call<AuthResponse> {
            return RetrofitClient.apiService.refreshToken(request)
        }
    }

}