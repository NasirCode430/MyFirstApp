package com.g.tragosapp.domain

import androidx.lifecycle.LiveData
import com.g.tragosapp.core.Resource
import com.g.tragosapp.data.model.Cocktail
import com.g.tragosapp.data.model.CocktailEntity
import kotlinx.coroutines.flow.Flow


interface DrinkRepository {
    suspend fun getCocktailByName(cocktailName: String): Flow<Resource<List<Cocktail>>>
    suspend fun saveFavoriteCocktail(cocktail: Cocktail)
    suspend fun isCocktailFavorite(cocktail: Cocktail): Boolean
    suspend fun getCachedCocktails(cocktailName: String): Resource<List<Cocktail>>
    suspend fun saveCocktail(cocktail: CocktailEntity)
    fun getFavoritesCocktails(): LiveData<List<Cocktail>>
    suspend fun deleteFavoriteCocktail(cocktail: Cocktail)
    suspend fun getCocktailByAlpha(cocktailName: String): Flow<Resource<List<Cocktail>>>
}