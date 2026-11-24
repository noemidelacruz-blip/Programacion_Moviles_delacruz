package com.delacruz.murocampus.data.remote

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Encabezado común para todas las peticiones: Accept: application/json.
 *
 * Se registra en RetrofitClient antes que el interceptor de registro para que
 * Logcat muestre también este encabezado. No depende de ninguna llamada en
 * concreto: cualquier método de PostApi lo recibe.
 */
class EncabezadoInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val peticion = chain.request()
            .newBuilder()
            .header("Accept", "application/json")
            .build()

        return chain.proceed(peticion)
    }
}
