package com.g.tragosapp.presentation

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.g.tragosapp.application.ToastHelper
import com.g.tragosapp.core.Resource
import com.g.tragosapp.domain.CocktailRepository
import com.g.tragosapp.data.model.Cocktail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class MainViewModel @ViewModelInject constructor(
    private val repository: CocktailRepository,
    private val toastHelper: ToastHelper,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val searchByName = MutableLiveData<Boolean>()
    private val currentCocktailName = MutableLiveData<String>()


    fun setCocktail(cocktailName: String) {
        currentCocktailName.value = cocktailName
    }
    fun setSearchByName(name: Boolean) {
        searchByName.value = name
    }


    val fetchCocktailList = currentCocktailName.distinctUntilChanged().switchMap { cocktailName ->
        liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(Resource.Loading)

            try {
                if (searchByName.value==true)
                {
                    repository.getCocktailByName(cocktailName).collect {

                        emit(it)
                    }
                }
                else{
                    repository.getCocktailByAlpha(cocktailName).collect {

                        emit(it)
                    }
                }

            } catch (e: Exception) {
                emit(Resource.Failure(e))
            }
        }
    }

    fun saveOrDeleteFavoriteCocktail(cocktail: Cocktail) {
        viewModelScope.launch {
            if (repository.isCocktailFavorite(cocktail)) {
                repository.deleteFavoriteCocktail(cocktail)
                toastHelper.sendToast("Cocktail deleted from favorites")
            } else {
                repository.saveFavoriteCocktail(cocktail)
                toastHelper.sendToast("Cocktail saved to favorites")
            }
        }
    }

    fun getFavoriteCocktails() =
        liveData<Resource<List<Cocktail>>>(viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(Resource.Loading)
            try {
                emitSource(repository.getFavoritesCocktails().map { Resource.Success(it) })
            } catch (e: Exception) {
                emit(Resource.Failure(e))
            }
        }

    fun deleteFavoriteCocktail(cocktail: Cocktail) {
        viewModelScope.launch {
            repository.deleteFavoriteCocktail(cocktail)
            toastHelper.sendToast("Cocktail deleted from favorites")
        }
    }

    suspend fun isCocktailFavorite(cocktail: Cocktail): Boolean =
        repository.isCocktailFavorite(cocktail)
}