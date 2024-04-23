package com.coolgirl.poctokkotlin.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    val Url = "http://45.154.1.94/"
    private var retrofit: Retrofit? = null
    val interseptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    val client = OkHttpClient.Builder().addInterceptor(interseptor).build()

    fun start():Retrofit{
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(Url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }
}