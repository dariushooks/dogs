package dariushooks.android.dogs.ui.breeds

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import dariushooks.android.dogs.Breed
import dariushooks.android.dogs.MainViewModel
import dariushooks.android.dogs.R
import dariushooks.android.dogs.adapters.BreedDetailAdapter
import dariushooks.android.dogs.architecture.Favorite
import dariushooks.android.dogs.dp
import kotlin.math.abs

class BreedsDetailFragment : Fragment(R.layout.fragment_breeds_detail)
{
    private lateinit var back : ImageView
    private lateinit var name : TextView
    private lateinit var imagePager : ViewPager2
    private lateinit var progress : ProgressBar
    private lateinit var add : MaterialButton
    private lateinit var breed : Breed

    private val cardMargin = 10.dp
    private val args : BreedsDetailFragmentArgs by navArgs()
    private val breedsViewModel : BreedsViewModel by activityViewModels()
    private val mainViewModel : MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        breed = args.breed
        breedsViewModel.loadImages(breed.main, breed.sub)

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        back = view.findViewById(R.id.back)
        back.setOnClickListener { findNavController().navigateUp() }

        progress = view.findViewById(R.id.loading)

        name = view.findViewById(R.id.breed_name)
        val text = "${breed.sub.ifBlank { "" }} ${breed.main}"
        name.text = text.uppercase().trim()

        add = view.findViewById(R.id.add_favorite)

        mainViewModel.getFavorites().observe(viewLifecycleOwner){

            add.apply {
                val favorite = it.find { fav -> fav.breed == text.trim() }
                if(favorite != null)
                {
                    this.text = this.context.getString(R.string.remove_favorite)
                }

                else
                {
                    this.text = this.context.getString(R.string.add_favorite)
                }

                setOnClickListener {
                    if(this.text == this.context.getString(R.string.remove_favorite))
                    {
                        mainViewModel.removeFavorite(favorite!!)
                        this.text = this.context.getString(R.string.add_favorite)
                        Snackbar.make(requireView(),
                            "${text.trim()} removed from favorites",
                            Snackbar.LENGTH_SHORT).show()
                    }

                    else
                    {
                        mainViewModel.addFavorite(Favorite(text.trim(), breed.image!!))
                        this.text = this.context.getString(R.string.remove_favorite)
                        Snackbar.make(requireView(),
                            "${text.trim()} added to favorites",
                            Snackbar.LENGTH_SHORT).show()
                    }

                }
            }
        }

        view.apply {
            if(text.trim() == "italian greyhound" )
            {
                setBackgroundColor(this.context.getColor(R.color.purple))
                name.setTextColor(this.context.getColor(R.color.white))
                back.setColorFilter(this.context.getColor(R.color.white))
            }

            else if(text.trim() == "whippet")
            {
                setBackgroundColor(this.context.getColor(R.color.pink))
                name.setTextColor(this.context.getColor(R.color.white))
                back.setColorFilter(this.context.getColor(R.color.white))
            }

        }

        imagePager = view.findViewById(R.id.image_pager)

        breedsViewModel.getImages().observe(viewLifecycleOwner){
            progress.isVisible = false
            imagePager.apply {
                clipChildren = false  // No clipping the left and right items
                clipToPadding = false  // Show the viewpager in full width without clipping the padding
                offscreenPageLimit = 3  // Render the left and right items
                setPageTransformer(CompositePageTransformer().apply {
                    addTransformer(MarginPageTransformer(cardMargin))
                    addTransformer{ page, position ->
                        val r = 1 - abs(position)
                        page.scaleY = (0.80f + r * 0.20f)
                    }
                })

                (getChildAt(0) as RecyclerView).overScrollMode =
                    RecyclerView.OVER_SCROLL_NEVER // Remove the scroll effect

                val photos =
                        if(it.size > 15)
                            it.shuffled().subList(0, 15)
                        else
                            it

                adapter = BreedDetailAdapter(photos)
            }
        }
    }
}