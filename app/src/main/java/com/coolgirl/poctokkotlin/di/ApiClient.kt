package com.coolgirl.poctokkotlin.di

import com.coolgirl.poctokkotlin.commons.plantApiPath
import com.coolgirl.poctokkotlin.data.ApiController
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun ApiClient(): ApiController {
    val interseptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    val client = OkHttpClient.Builder().addInterceptor(interseptor).build()

    return Retrofit.Builder()
        .baseUrl(plantApiPath + "/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiController::class.java)

}

fun PlantApiClient(): ApiController {
    val interseptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    val client = OkHttpClient.Builder().addInterceptor(interseptor).build()

    return Retrofit.Builder()
        .baseUrl(plantApiPath + "/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiController::class.java)

}