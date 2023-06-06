package dariushooks.android.dogs.architecture

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Favorite::class], version = 1)
abstract class FavoritesDatabase : RoomDatabase()
{
    abstract fun favoritesDao() : FavoritesDao

    companion object
    {
        private var instance : FavoritesDatabase? = null
        private const val DATABASE_NAME = "FAVORITES_DATABASE"

        fun getInstance(context : Context) : FavoritesDatabase
        {
            return instance ?: synchronized(this) {
                val newInstance = Room.databaseBuilder(context.applicationContext,
                    FavoritesDatabase::class.java,
                    DATABASE_NAME).build()
                instance = newInstance
                newInstance
            }
        }
    }
}