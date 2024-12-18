package com.example.evalkotlin.data.datasource

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    // Retrofit pour les requêtes HTTP

    //base de l'api
    private const val BASE_URL = "https://tyradex.app/api/v1/"

    // le service de l'api
    val api: ApiService by lazy {
        // Ajouter un intercepteur de logging
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Log des headers et du corps de la requête/réponse
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor) // Ajouter l'intercepteur de logging
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)  // Utilisation de la BASE_URL correctement formatée
            .addConverterFactory(GsonConverterFactory.create()) // Convertisseur JSON
            .client(client)
            .build()
            .create(ApiService::class.java)
    }
}