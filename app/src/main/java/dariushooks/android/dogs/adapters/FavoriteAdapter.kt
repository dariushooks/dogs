package dariushooks.android.dogs.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import dariushooks.android.dogs.Breed
import dariushooks.android.dogs.R
import dariushooks.android.dogs.architecture.Favorite

class FavoriteAdapter(private val favorites : List<Favorite>,
                      private val breedClick : (breed : Breed) -> Unit,
                      private val addFavorite : (breed : String, image : String) -> Unit,
                      private val removeFavorite : (fav : Favorite) -> Unit)
    : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_breed, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) { holder.bind(favorites[position]) }

    override fun getItemCount(): Int = favorites.size

    inner class FavoriteViewHolder(itemView : View) : ViewHolder(itemView)
    {
        private val name : TextView = itemView.findViewById(R.id.breed_name)
        private var favorite : ImageFilterView = itemView.findViewById(R.id.breed_favorite)
        private val image : ImageView = itemView.findViewById(R.id.breed_image)

        fun bind(fav : Favorite)
        {
            name.text = fav.breed.uppercase()
            favorite.crossfade = 1F
            Glide.with(itemView.context).load(fav.image).into(image)
            itemView.apply {
                if(fav.breed.trim() == "italian greyhound" )
                    backgroundTintList = ColorStateList.valueOf(this.context.getColor(R.color.purple))
                else if(fav.breed.trim() == "whippet")
                    backgroundTintList = ColorStateList.valueOf(this.context.getColor(R.color.pink))

                setOnClickListener {
                    if(fav.breed.trim().contains(" "))
                    {
                        val (sub, main) = fav.breed.trim().split(" ")
                        breedClick(Breed(main, sub, fav.image))
                    }

                    else
                        breedClick(Breed(fav.breed.trim(), "", fav.image))
                }
            }
        }
    }
}