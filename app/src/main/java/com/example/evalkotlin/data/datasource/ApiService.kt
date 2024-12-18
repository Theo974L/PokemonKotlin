package com.example.evalkotlin.data.datasource

import Pokemon
import PokemonDetails
import okhttp3.Challenge
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    // Les routes vers l'api
    @GET("pokemon")
    suspend fun listingPokemon(): Response<List<Pokemon>>

    @GET("pokemon/{id}")
    suspend fun getPokemonById(@Path("id") id: Int): Response<PokemonDetails>

}