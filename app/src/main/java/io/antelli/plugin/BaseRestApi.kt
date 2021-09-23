package io.antelli.plugin

import com.google.gson.Gson
import com.google.gson.GsonBuilder

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Handcrafted by Štěpán Šonský on 14.10.2017.
 */

abstract class BaseRestApi<T : Any> protected constructor() : BaseApi<T>() {

    init {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttp = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

        val gson = GsonBuilder().create()

        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttp)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        api = retrofit.create(apiDefClass.java)
    }
}
