package com.oxygensend.shoppinglist.api

import com.github.leonardoxh.livedatacalladapter.LiveDataCallAdapterFactory
import com.github.leonardoxh.livedatacalladapter.LiveDataResponseBodyConverterFactory
import com.oxygensend.shoppinglist.api.context.Context
import com.oxygensend.shoppinglist.api.context.ContextKeys
import com.oxygensend.shoppinglist.api.dto.RefreshTokenRequest
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    companion object {

        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(API.BASE_URL)
                .addCallAdapterFactory(LiveDataCallAdapterFactory.create())
                .addConverterFactory(LiveDataResponseBodyConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        private val okHttpClient: OkHttpClient by lazy {
            OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer ${Context.getString(ContextKeys.ACCESS_TOKEN)}")
                        .build()
                    val response = chain.proceed(request)

                    if (response.code == 401) {
                        // Make a request to refresh the access token using the refresh token
                        val refreshToken = Context.getString(ContextKeys.REFRESH_TOKEN)
                        val authResponse = AuthClient.refreshToken(RefreshTokenRequest(refreshToken)).execute()

                        if (!authResponse.isSuccessful) {
                            // Redirect to LoginActivity
                            Context.clear()
                            return@addInterceptor response
                        }

                        // Save the new access token
                        authResponse.body()?.accessToken?.let { Context.saveString(ContextKeys.ACCESS_TOKEN, it) }
                        authResponse.body()?.refreshToken?.let { Context.saveString(ContextKeys.REFRESH_TOKEN, it) }

                        // Retry the original request with the new access token
                        val newRequest = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer ${Context.getString(ContextKeys.ACCESS_TOKEN)}")
                            .build()

                        return@addInterceptor chain.proceed(newRequest)
                    }

                    response
                }
                .build()
        }

        val apiService: ApiService by lazy {
            retrofit.create(ApiService::class.java)
        }

        val authenticatedService: ApiService by lazy {
            retrofit.newBuilder()
                .client(okHttpClient)
                .build()
                .create(ApiService::class.java)
        }

    }
}