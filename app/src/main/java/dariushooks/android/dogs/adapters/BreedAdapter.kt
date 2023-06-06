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

class BreedAdapter(private val breeds : List<Breed>,
                   private val favorites : ArrayList<Favorite>,
                   private val breedClick : (breed : Breed) -> Unit,
                   private val addFavorite : (breed : String, image : String) -> Unit,
                   private val removeFavorite : (fav : Favorite) -> Unit)
    : RecyclerView.Adapter<BreedAdapter.BreedViewHolder>()
{
    init {
        //setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreedViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_breed, parent, false)
        return BreedViewHolder(view)
    }

    override fun onBindViewHolder(holder: BreedViewHolder, position: Int) { holder.bind(breeds[position]) }

    override fun getItemCount(): Int = breeds.size

    fun updateFavorites(favorites : List<Favorite>)
    {
        this.favorites.clear()
        this.favorites.addAll(favorites)
    }

    inner class BreedViewHolder(itemView : View) : ViewHolder(itemView)
    {
        private val name : TextView = itemView.findViewById(R.id.breed_name)
        private var favorite : ImageFilterView = itemView.findViewById(R.id.breed_favorite)
        private val image : ImageView = itemView.findViewById(R.id.breed_image)

        fun bind(breed : Breed)
        {
            val text = "${breed.sub.ifBlank { "" }} ${breed.main}"
            name.text = text.uppercase().trim()

            itemView.apply {
                if(text.trim() == "italian greyhound" )
                    backgroundTintList = ColorStateList.valueOf(this.context.getColor(R.color.purple))
                else if(text.trim() == "whippet")
                    backgroundTintList = ColorStateList.valueOf(this.context.getColor(R.color.pink))

                setOnClickListener { breedClick(breed) }
            }

            val fav = favorites.find { it.breed == text.trim() }
            if(fav != null)
                favorite.crossfade = 1F

            favorite.setOnClickListener {
                val view = it as ImageFilterView
                if(view.crossfade == 0F)
                {
                    addFavorite(text, breed.image!!)
                    favorites.add(Favorite(text.trim(), breed.image))
                    notifyItemChanged(adapterPosition)
                    view.crossfade = 1F
                }

                else
                {
                    removeFavorite(fav!!)
                    favorites.remove(fav)
                    notifyItemChanged(adapterPosition)
                    view.crossfade = 0F
                }

            }

            Glide.with(image.context).load(breed.image).into(image)
        }
    }
}