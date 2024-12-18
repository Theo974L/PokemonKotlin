package com.example.evalkotlin.data.repositories

import Pokemon
import PokemonDetails
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.evalkotlin.data.datasource.ApiService

class PokemonRepository(private val apiService: ApiService) {

    suspend fun getPokemonById(id: Int): Result<PokemonDetails> {
        return try {
            val response = apiService.getPokemonById(id)
            if (response.isSuccessful) {
                response.body()?.let { body ->
                    val pokemonData = PokemonDetails(
                        pokedex_id = body.pokedex_id,
                        category = body.category,
                        name = body.name,
                        sprites = body.sprites,
                        height = body.height,
                        weight = body.weight,
                        talents = body.talents
                    )
                    Result.success(pokemonData)
                } ?: Result.failure(Exception("Le corps de la réponse est nul"))
            } else {
                Result.failure(Exception("Erreur ${response.code()} : ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPokemonList(): Result<List<Pokemon>> {
        return try {
            val response = apiService.listingPokemon()
            if (response.isSuccessful) {
                response.body().let { body ->
                    val pokemonList = body!!.take(10).map { apiPokemon ->
                        Pokemon(
                            pokedex_id = apiPokemon.pokedex_id,
                            category = apiPokemon.category,
                            name = apiPokemon.name,
                            sprites = apiPokemon.sprites
                        )
                    }
                    Result.success(pokemonList)
                }
            } else {
                Result.failure(Exception("Erreur ${response.code()} : ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}