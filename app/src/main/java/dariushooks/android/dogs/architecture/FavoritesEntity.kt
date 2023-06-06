package dariushooks.android.dogs.architecture

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Favorite(@ColumnInfo(name = "breed") val breed : String,
                    @ColumnInfo(name = "image") val image : String,
                    @PrimaryKey(autoGenerate = true) var id : Int = 0)