package dariushooks.android.dogs.api

import dariushooks.android.dogs.BreedImage
import dariushooks.android.dogs.BreedImages
import dariushooks.android.dogs.Breeds
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiRequest
{
    companion object{
        const val BASE_URL = "https://dog.ceo/api/"
    }

    @GET("breeds/list/all")
    suspend fun getAllBreeds() : Response<Breeds>

    //Main breed
    //
    @GET("breed/{main_breed}/images/random")
    suspend fun getRandomImage(
        @Path("main_breed") main : String) : Response<BreedImage>

    @GET("breed/{main_breed}/images")
    suspend fun getBreedImages(
        @Path("main_breed") main : String) : Response<BreedImages>

    //Sub breed
    //
    @GET("breed/{main_breed}/{sub_breed}/images/random")
    suspend fun getRandomImage(
        @Path("main_breed") main : String,
        @Path("sub_breed") sub : String) : Response<BreedImage>

    @GET("breed/{main_breed}/{sub_breed}/images")
    suspend fun getBreedImages(
        @Path("main_breed") main : String,
        @Path("sub_breed") sub : String) : Response<BreedImages>
}