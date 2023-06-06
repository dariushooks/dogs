package dariushooks.android.dogs.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import dariushooks.android.dogs.R

class BreedDetailAdapter(private val images : List<String>) : RecyclerView.Adapter<BreedDetailAdapter.BreedDetailViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreedDetailViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_breed_detail, parent, false)
        return BreedDetailViewHolder(view)
    }

    override fun onBindViewHolder(holder: BreedDetailViewHolder, position: Int) { holder.bind(images[position]) }

    override fun getItemCount(): Int = images.size

    inner class BreedDetailViewHolder(itemView : View) : ViewHolder(itemView)
    {
        private val breedImage : ImageView = itemView.findViewById(R.id.breed_image)

        fun bind(image : String)
        {
            breedImage.apply {
                Glide.with(this).load(image).into(this)
            }
        }
    }
}