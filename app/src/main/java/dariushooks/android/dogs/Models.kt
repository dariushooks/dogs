package dariushooks.android.dogs

import android.content.res.Resources
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Breed(val main : String, val sub : String, val image : String?) : Parcelable
data class Breeds(@SerializedName("message") val data : Map<String, List<String>>)
data class BreedImage(@SerializedName("message") val data : String)
data class BreedImages(@SerializedName("message") val data : List<String>)

val Int.dp : Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()