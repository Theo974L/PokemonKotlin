package com.example.evalkotlin.ui.screens

import Pokemon
import android.media.MediaPlayer
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.evalkotlin.R
import com.example.evalkotlin.data.datasource.RetrofitInstance
import com.example.evalkotlin.ui.theme.BackgroundColor
import com.example.evalkotlin.ui.theme.SecondaryColor
import com.example.evalkotlin.ui.theme.TertiaryColor
import com.example.evalkotlin.ui.theme.onBackgroundColor
import com.example.evalkotlin.ui.theme.onTertiaryColor
import kotlinx.coroutines.launch


/**
 *
 * @author Theo laforge
 * @version 1.0
 *
 */

@Composable
fun AccueilScreen(navController: NavController) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(onTertiaryColor)
    ) {
        // Titre principal
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(BackgroundColor)
                .padding(16.dp)
        ) {
            Text(
                text = context.getString(R.string.TitreAccueil),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = onBackgroundColor,
                modifier = Modifier
            )
        }

        // Espacement entre le titre et le contenu
        Spacer(modifier = Modifier.height(16.dp))

        // Liste des Pokémon
        PokemonListing(navController)
    }
}

@Composable
fun PokemonListing(navController: NavController) {
    val scope = rememberCoroutineScope()
    val pokemonList = remember { mutableStateOf<List<Pokemon>>(emptyList()) }
    val isLoading = remember { mutableStateOf(true) }


    // A mettre dans le repo (Si j'ai le temps)

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                Log.v("TAG", "Calling API -----------------------------")

                val response = RetrofitInstance.api.listingPokemon()

                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        Log.v("TAG", "API Call Success!")
                        pokemonList.value = body.take(10).map { apiPokemon ->
                            Pokemon(
                                pokedex_id = apiPokemon.pokedex_id,
                                category = apiPokemon.category,
                                name = apiPokemon.name,
                                sprites = apiPokemon.sprites
                            )
                        }
                    }
                }
            } catch (error: Exception) {
                Log.v("TAG", "API Call Error: $error")
                error.printStackTrace()
            } finally {
                isLoading.value = false
                Log.v("TAG", "Finished loading")
            }
        }
    }

    // Affichage de l'indicateur de chargement
    if (isLoading.value) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(modifier = Modifier.size(50.dp), color = MaterialTheme.colorScheme.primary)
        }
    } else {
        // Affichage des Pokémon dans une LazyColumn
        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            items(pokemonList.value) { pokemon ->
                PokemonCard(pokemon, navController)
            }
        }
    }
}

@Composable
fun PokemonCard(pokemon: Pokemon,navController: NavController) {
    // La carte du Pokémon avec un design moderne et élégant
    val context = LocalContext.current
    val vibrator = context.getSystemService(Vibrator::class.java)
    var mediaPlayer: MediaPlayer? = remember { null }
    var isPlaying by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                navController.navigate("details/${pokemon.pokedex_id}")
                // Vibration lors du clic sur la carte
                if (vibrator != null && vibrator.hasVibrator()) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
                    } else {
                        vibrator.vibrate(200)
                    }
                }
                if (mediaPlayer == null){
                    mediaPlayer = MediaPlayer.create(context, R.raw.fart)
                    mediaPlayer?.setOnCompletionListener {
                        isPlaying = false
                    }
                }
                if (!isPlaying){
                    mediaPlayer?.start();
                    isPlaying = true;
                }else{
                    mediaPlayer?.stop();
                    isPlaying = false;
                }
            }
            .background(MaterialTheme.colorScheme.surface, shape = MaterialTheme.shapes.medium),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image du Pokémon
            Image(
                painter = rememberAsyncImagePainter(pokemon.sprites.regular),
                contentDescription = pokemon.name.fr,
                modifier = Modifier
                    .size(80.dp)
                    .padding(end = 16.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer, shape = MaterialTheme.shapes.small)
                    .padding(8.dp)
            )

            // Détails du Pokémon (Nom et catégorie)
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(end = 16.dp)
            ) {
                Text(
                    text = pokemon.name.fr,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = pokemon.category,
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun PreviewPokemonListing(navController: NavController) {
    Box(
        modifier = Modifier
            .background(BackgroundColor)
    ){
        Text(
            text = "Liste des Pokémon",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = Color.White,
            modifier = Modifier
                .padding(16.dp)
                .background(SecondaryColor)
        )
        MaterialTheme {
            PokemonListing(navController)
        }
    }

}
