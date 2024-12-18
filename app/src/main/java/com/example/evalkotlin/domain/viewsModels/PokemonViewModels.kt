package com.example.evalkotlin.domain.viewsModels

import Pokemon
import PokemonDetails
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evalkotlin.data.repositories.PokemonRepository
import kotlinx.coroutines.launch

class PokemonViewModels(private val repository: PokemonRepository) : ViewModel() {

    private val _pokemonList = MutableLiveData<List<Pokemon>>()
    val pokemonList: LiveData<List<Pokemon>> = _pokemonList

    private val _selectedPokemon = MutableLiveData<PokemonDetails?>()
    val selectedPokemon: LiveData<PokemonDetails?> = _selectedPokemon

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun fetchPokemonList() {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.getPokemonList()
            result.onSuccess { pokemons ->
                _pokemonList.value = pokemons
            }.onFailure { e ->
                _error.value = e.message
            }
            _loading.value = false
        }
    }

    fun fetchPokemonById(id: Int) {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.getPokemonById(id)
            result.onSuccess { pokemonDetails ->
                _selectedPokemon.value = pokemonDetails
            }.onFailure { e ->
                _error.value = e.message
            }
            _loading.value = false
        }
    }
}
