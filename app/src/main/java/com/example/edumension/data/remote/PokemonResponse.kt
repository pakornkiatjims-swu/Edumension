package com.example.edumension.data.remote

import com.google.gson.annotations.SerializedName

data class PokemonResponse(
    val id: Int,
    val name: String,
    val sprites: Sprites,
    val types: List<TypeElement>,
    @SerializedName("base_experience")
    val baseExperience: Int
)

data class Sprites(
    val other: OtherSprites
)

data class OtherSprites(
    @SerializedName("official-artwork")
    val officialArtwork: OfficialArtwork
)

data class OfficialArtwork(
    @SerializedName("front_default")
    val frontDefault: String?
)

data class TypeElement(
    val type: TypeDetail
)

data class TypeDetail(
    val name: String
)
