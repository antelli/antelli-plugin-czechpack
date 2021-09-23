package io.antelli.plugin

import com.google.gson.Gson
import com.google.gson.GsonBuilder

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.reflect.KClass

/**
 * Handcrafted by Štěpán Šonský on 14.10.2017.
 */

abstract class BaseApi<T : Any> protected constructor() {

    protected lateinit var api: T

    protected abstract val baseUrl: String
    protected abstract val apiDefClass: KClass<T>
}
