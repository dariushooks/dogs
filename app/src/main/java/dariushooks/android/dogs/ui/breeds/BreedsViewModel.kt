package dariushooks.android.dogs.ui.breeds

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dariushooks.android.dogs.api.ApiRequest
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BreedsViewModel : ViewModel()
{
    private val images = MutableLiveData<List<String>>()

    fun getImages() : LiveData<List<String>> = images

    fun loadImages(main : String, sub : String)
    {
        viewModelScope.launch {
            val retrofit = Retrofit.Builder()
                .baseUrl(ApiRequest.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()

            val apiRequest = retrofit.create(ApiRequest::class.java)
            val data =
                if(sub.isEmpty())
                    apiRequest.getBreedImages(main)
                else
                    apiRequest.getBreedImages(main, sub)
            val list = data.body()?.data
            images.value = list!!
        }
    }
}