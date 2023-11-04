package com.oxygensend.shoppinglist.api

import com.github.leonardoxh.livedatacalladapter.LiveDataCallAdapterFactory
import com.github.leonardoxh.livedatacalladapter.LiveDataResponseBodyConverterFactory
import com.oxygensend.shoppinglist.api.context.Context
import com.oxygensend.shoppinglist.api.context.ContextKeys
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
                    chain.proceed(request)
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