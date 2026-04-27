package com.example.edumension.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeApiService {
    @GET("pokemon/{idOrName}")
    suspend fun getPokemon(@Path("idOrName") idOrName: String): PokemonResponse
}

object RetrofitClient {
    private const val BASE_URL = "https://pokeapi.co/api/v2/"

    val instance: PokeApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(PokeApiService::class.java)
    }
}
