/**
 *
 * Data class pour pouvoir mapper les données de la requete API
 *
 */

data class Pokemon(
    val pokedex_id: Int,  // ID du Pokémon
    val category: String, // Catégorie du Pokémon
    val name: PokemonName, // Nom du Pokémon (en plusieurs langues)
    val sprites: Sprite // Image du Pokémon (l'URL de l'image)
)

data class PokemonName(
    val fr: String, // Nom en français
    val en: String, // Nom en anglais
    val jp: String  // Nom en japonais
)

data class Sprite(
    val regular: String, // URL de l'image
)

data class PokemonDetails(
    val pokedex_id: Int,  // ID du Pokémon
    val category: String, // Catégorie du Pokémon
    val name: PokemonName, // Nom du Pokémon (en plusieurs langues)
    val sprites: Sprite,
    val height: String, // Hauteur du Pokémon
    val weight: String, // Poids du Pokémon
    val talents: List<Talents>, // Talent du Pokémon
)

data class Talents(
    val name: String,
    val tc: String
)
