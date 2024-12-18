package com.example.evalkotlin

import PokemonDetails
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.evalkotlin.ui.Routes
import com.example.evalkotlin.ui.screens.AccueilScreen
import com.example.evalkotlin.ui.screens.PokemonDetails
import com.example.evalkotlin.ui.theme.LeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LeTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = Routes.accueil) {
                    composable(Routes.accueil) {
                        AccueilScreen(navController)
                    }
                    composable("details/{pokedex_id}") { backStackEntry ->
                        // Récupérer l'id du Pokémon depuis l'argument de la route
                        val pokedexId = backStackEntry.arguments?.getString("pokedex_id")?.toInt()

                        if (pokedexId != null) {
                            // Afficher les détails du Pokémon avec l'ID
                            PokemonDetails(navController, pokedexId)
                        }
                    }
                }
            }
        }
    }
}
