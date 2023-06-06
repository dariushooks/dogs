package dariushooks.android.dogs.architecture

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao
{
    @Query("SELECT * FROM favorite")
    fun getFavorites() : Flow<List<Favorite>>

    @Insert
    suspend fun addFavorite(favorite : Favorite)

    @Delete
    suspend fun removeFavorite(favorite : Favorite)
}