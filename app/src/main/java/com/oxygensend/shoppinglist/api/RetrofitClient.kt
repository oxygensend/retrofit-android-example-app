package com.oxygensend.shoppinglist.api

import android.util.Log
import com.github.leonardoxh.livedatacalladapter.LiveDataCallAdapterFactory
import com.github.leonardoxh.livedatacalladapter.LiveDataResponseBodyConverterFactory
import com.oxygensend.shoppinglist.api.context.Context
import com.oxygensend.shoppinglist.api.context.ContextKeys
import com.oxygensend.shoppinglist.api.dto.RefreshTokenRequest
import okhttp3.Interceptor
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

        private fun loggingInterceptor(): Interceptor {
            return Interceptor { chain ->
                val request = chain.request()
                Log.d(
                    "RetrofitClient",
                    "Making a request to: ${request.method} ${request.url} "
                )
                chain.proceed(request)
            }
        }

        private fun authorizationInterceptor(): Interceptor {
            return Interceptor { chain ->
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
                        return@Interceptor response
                    }

                    // Save the new access token
                    authResponse.body()?.accessToken?.let { Context.saveString(ContextKeys.ACCESS_TOKEN, it) }
                    authResponse.body()?.refreshToken?.let { Context.saveString(ContextKeys.REFRESH_TOKEN, it) }

                    // Retry the original request with the new access token
                    val newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer ${Context.getString(ContextKeys.ACCESS_TOKEN)}")
                        .build()

                    return@Interceptor chain.proceed(newRequest)
                }

                response
            }
        }

        private val okHttpClient: OkHttpClient by lazy {
            OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor())
                .build()
        }

        val apiService: ApiService by lazy {
            retrofit.newBuilder()
                .client(okHttpClient)
                .build()
                .create(ApiService::class.java)
        }

        val authenticatedService: ApiService by lazy {
            val client = okHttpClient.newBuilder()
                .addInterceptor(authorizationInterceptor())
                .build()
            retrofit.newBuilder()
                .client(client)
                .build()
                .create(ApiService::class.java)
        }

    }
}