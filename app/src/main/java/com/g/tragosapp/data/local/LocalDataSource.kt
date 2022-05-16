package com.g.tragosapp.data.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.g.tragosapp.core.Resource
import com.g.tragosapp.data.model.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class LocalDataSource @Inject constructor(private val drinkDao: DrinkDao) {
    suspend fun saveFavoriteCocktail(cocktail: Cocktail) {
        return drinkDao.saveFavoriteCocktail(cocktail.asFavoriteEntity())
    }

    fun getFavoritesCocktails(): LiveData<List<Cocktail>> {
        return drinkDao.getAllFavoriteDrinksWithChanges().map { it.asDrinkList() }
    }

    suspend fun deleteCocktail(cocktail: Cocktail) {
        return drinkDao.deleteFavoriteCoktail(cocktail.asFavoriteEntity())
    }

    suspend fun saveCocktail(cocktail: CocktailEntity) {
        drinkDao.saveCocktail(cocktail)
    }

    suspend fun getCachedCocktails(cocktailName: String): Resource<List<Cocktail>> {
        return Resource.Success(drinkDao.getCocktails(cocktailName).asCocktailList())
    }

    suspend fun isCocktailFavorite(cocktail: Cocktail): Boolean {
        return drinkDao.getCocktailById(cocktail.cocktailId) != null
    }
}