package com.coolgirl.poctokkotlin.di

import com.coolgirl.poctokkotlin.commons.plantApiPath
import com.coolgirl.poctokkotlin.commons.plantIdApiPath
import com.coolgirl.poctokkotlin.data.ApiController
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun ApiClient(): ApiController {
    val interceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader("Accept", "application/json")
            .build()
        chain.proceed(request)
    }

    val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    return Retrofit.Builder()
        .baseUrl(plantApiPath + "/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiController::class.java)

}

fun PlantIdApiClient(): ApiController {
    val interceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader("Accept", "application/json")
            .build()
        chain.proceed(request)
    }

    val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    return Retrofit.Builder()
        .baseUrl(plantIdApiPath)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiController::class.java)

}