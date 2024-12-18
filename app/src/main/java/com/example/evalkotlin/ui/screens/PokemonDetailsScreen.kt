package com.example.evalkotlin.ui.screens

import Pokemon
import PokemonDetails
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.evalkotlin.R
import com.example.evalkotlin.data.datasource.RetrofitInstance
import com.example.evalkotlin.ui.theme.BackgroundColor
import com.example.evalkotlin.ui.theme.SecondaryColor
import kotlinx.coroutines.launch

/**
 *
 * @author Théo Laforge
 * @version 1.0
 *
 */

@Composable
fun PokemonDetails(
    navController: NavController,
    pokedexId: Int
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(16.dp)
    ) {
        // Titre de l'écran
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(SecondaryColor)
                .padding(16.dp)
        ) {
            Text(
                text = context.getString(R.string.TitreDetails),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Contenu des détails
        GetPokemon(pokedexId)
    }
}


@Composable
fun GetPokemon(
    pokemon: Int
){
    val scope = rememberCoroutineScope()
    val pokemonData = remember { mutableStateOf<PokemonDetails?>(null) }
    val isLoading = remember { mutableStateOf(true) }



    // A mettre dans le repo (Si j'ai le temps)

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                Log.d("TAG", "Calling API to get details of Pokémon with ID: $pokemon")
                val response = RetrofitInstance.api.getPokemonById(pokemon)

                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        pokemonData.value = PokemonDetails(
                            pokedex_id = body.pokedex_id,
                            category = body.category,
                            name = body.name,
                            sprites = body.sprites,
                            height = body.height,
                            weight = body.weight,
                            talents = body.talents
                        )
                    }
                }
            } catch (error: Exception) {
                Log.e("TAG", "API Call Error: $error")
                error.printStackTrace()
            } finally {
                isLoading.value = false
            }
        }
    }

    // Indicateur de chargement
    if (isLoading.value) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(50.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }
    } else {
        ShowPokemon(pokemonData.value)
    }
}

@Composable
fun ShowPokemon(pokemonDetails: PokemonDetails?) {
    if (pokemonDetails != null) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Afficher l'image
            item {
                Image(
                    painter = rememberAsyncImagePainter(pokemonDetails.sprites.regular),
                    contentDescription = pokemonDetails.name.fr,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .background(MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.medium)
                        .padding(16.dp)
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Détails principaux
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "#${pokemonDetails.pokedex_id} - ${pokemonDetails.name.fr}",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = pokemonDetails.category,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        InfoItem(label = "Hauteur", value = "${pokemonDetails.height} cm")
                        InfoItem(label = "Poids", value = "${pokemonDetails.weight} kg")
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Talents
            item {
                Text(
                    text = "Talents",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            items(pokemonDetails.talents) { talent ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    InfoItem(label = "Nom :", value = talent.name)
                    InfoItem(label = "Tc :", value = talent.tc)
                }
            }
        }
    }
}

@Composable
fun InfoItem(label: String, value: String) {
    Text(
        text = "$label $value",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
    )
}
