package com.delacruz.catalogo.data.remote

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Crea y configura Retrofit una sola vez para toda la app.
 *
 * La baseUrl SIEMPRE termina en "/" (si falta, Retrofit lanza excepción).
 */
object RetrofitClient {
//RetrofitClient configura la conexión con la API.
    private const val BASE_URL = "https://dummyjson.com/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        // Muestra en Logcat la URL y el código de respuesta de cada llamada.
        level = HttpLoggingInterceptor.Level.BASIC
    }

    private val clienteHttp = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    private val gson = GsonBuilder().create()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(clienteHttp)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService::class.java)
    }
}
