package dariushooks.android.dogs

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dariushooks.android.dogs.api.ApiRequest
import dariushooks.android.dogs.architecture.Favorite
import dariushooks.android.dogs.architecture.FavoritesDatabase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainViewModel(application : Application) : AndroidViewModel(application)
{
    private val breeds = MutableLiveData<List<Breed>>()
    private val favorites = MutableLiveData<List<Favorite>>()
    private val pageSize = 10
    private val database = FavoritesDatabase.getInstance(application)
    private val dao = database.favoritesDao()

    init {
        loadBreeds()
        viewModelScope.launch {
            dao.getFavorites().collectLatest {
                favorites.value = it
            }
        }
    }

    fun getBreeds() : LiveData<List<Breed>> = breeds
    fun getFavorites() : LiveData<List<Favorite>> = favorites

    fun addFavorite(favorite : Favorite)
    {
        viewModelScope.launch {dao.addFavorite(favorite) }
    }

    fun removeFavorite(favorite : Favorite)
    {
        viewModelScope.launch { dao.removeFavorite(favorite) }
    }

    private fun loadBreeds()
    {
        viewModelScope.launch {
            val retrofit = Retrofit.Builder()
                .baseUrl(ApiRequest.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()

            val apiRequest = retrofit.create(ApiRequest::class.java);
            val data = apiRequest.getAllBreeds().body()
            val last = data?.data?.entries?.last()
            val list = mutableListOf<Breed>()
            data?.data?.entries?.forEach {
                if(it.value.isNotEmpty())
                    it.value.forEach { sub ->
                        val image = apiRequest.getRandomImage(it.key, sub).body()?.data
                        list.add(Breed(it.key, sub, image))
                        if(list.size.mod(pageSize) == 0 || it == last)
                            breeds.value = list
                    }

                else
                {
                    val image = apiRequest.getRandomImage(it.key).body()?.data
                    list.add(Breed(it.key, "", image))
                    if(list.size.mod(pageSize) == 0 || it == last)
                        breeds.value = list
                }
            }
        }
    }
}