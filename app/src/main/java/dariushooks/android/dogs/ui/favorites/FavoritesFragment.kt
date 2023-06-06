package dariushooks.android.dogs.ui.favorites

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dariushooks.android.dogs.Breed
import dariushooks.android.dogs.MainViewModel
import dariushooks.android.dogs.R
import dariushooks.android.dogs.adapters.FavoriteAdapter
import dariushooks.android.dogs.architecture.Favorite
import dariushooks.android.dogs.ui.breeds.BreedsFragmentDirections

class FavoritesFragment : Fragment(R.layout.fragment_favorites)
{
    private lateinit var breedsList : RecyclerView
    private lateinit var progress : ProgressBar

    private var lastIndex = 0
    private val favorites = ArrayList<Favorite>()
    private val mainViewModel : MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        breedsList = view.findViewById(R.id.breeds_list)
        progress = view.findViewById(R.id.loading)

        breedsList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = FavoriteAdapter(favorites, ::breedClick, ::addFavorite, ::removeFavorite)
        }

        mainViewModel.getFavorites().observe(viewLifecycleOwner){
            progress.isVisible = false
            lastIndex = it.size
            val diff =
                if(it.isEmpty())
                    it
                else
                    it.subtract(favorites.toSet())
            favorites.addAll(diff)
            breedsList.adapter?.notifyItemRangeInserted(lastIndex, diff.size)
        }
    }

    private fun breedClick(breed : Breed)
    {
        val action = FavoritesFragmentDirections.actionFragmentFavoritesToFragmentBreedsDetail(breed)
        findNavController().navigate(action)
    }

    private fun addFavorite(breed : String, image : String)
    {
        mainViewModel.addFavorite(Favorite(breed, image))
    }

    private fun removeFavorite(fav : Favorite)
    {
        mainViewModel.removeFavorite(fav)
    }
}