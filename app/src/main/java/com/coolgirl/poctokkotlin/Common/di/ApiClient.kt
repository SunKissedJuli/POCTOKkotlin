package com.coolgirl.poctokkotlin.Common.di

import com.coolgirl.poctokkotlin.api.ApiController
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun ApiClient(): ApiController {
    val Url = "http://45.154.1.94/"
    val interseptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    val client = OkHttpClient.Builder().addInterceptor(interseptor).build()

    return Retrofit.Builder()
        .baseUrl(Url)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiController::class.java)

}