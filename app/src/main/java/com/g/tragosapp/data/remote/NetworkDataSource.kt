package com.g.tragosapp.data.remote

import com.g.tragosapp.core.Resource
import com.g.tragosapp.data.model.Cocktail
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject


@ExperimentalCoroutinesApi
class NetworkDataSource @Inject constructor(
    private val webService: WebService
) {
    suspend fun getCocktailByName(cocktailName: String): Flow<Resource<List<Cocktail>>> =
        callbackFlow {
            trySend(
                Resource.Success(
                    webService.getCocktailByName(cocktailName)?.cocktailList ?: listOf()
                )
            ).isSuccess
            awaitClose { close() }
        }
    suspend fun getCocktailByAlpha(cocktailName: String): Flow<Resource<List<Cocktail>>> =
        callbackFlow {
            trySend(
                Resource.Success(
                    webService.getCocktailByAlpha(cocktailName)?.cocktailList ?: listOf()
                )
            ).isSuccess
            awaitClose { close() }
        }
}