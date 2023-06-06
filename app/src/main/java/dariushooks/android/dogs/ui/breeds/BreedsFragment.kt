package dariushooks.android.dogs.ui.breeds

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dariushooks.android.dogs.MainViewModel
import dariushooks.android.dogs.Breed
import dariushooks.android.dogs.R
import dariushooks.android.dogs.adapters.BreedAdapter
import dariushooks.android.dogs.architecture.Favorite

class BreedsFragment : Fragment(R.layout.fragment_breeds)
{
    private lateinit var search : SearchView
    private lateinit var breedsList : RecyclerView
    private lateinit var progress : ProgressBar

    private var lastIndex = 0
    private val breeds = ArrayList<Breed>()
    private val favorites = ArrayList<Favorite>()
    private val mainViewModel : MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {

            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        search = view.findViewById(R.id.breed_search)
        breedsList = view.findViewById(R.id.breeds_list)
        progress = view.findViewById(R.id.loading)

        breedsList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = BreedAdapter(breeds, favorites, ::breedClick, ::addFavorite, ::removeFavorite)
        }

        mainViewModel.getFavorites().observe(viewLifecycleOwner){
            favorites.clear()
            favorites.addAll(it)
            (breedsList.adapter as BreedAdapter).updateFavorites(it)
        }

        mainViewModel.getBreeds().observe(viewLifecycleOwner){
            progress.isVisible = false
            lastIndex = it.size
            val diff =
                if(it.isEmpty())
                    it
                else
                    it.subtract(breeds.toSet())
            breeds.addAll(diff)
            breedsList.adapter?.notifyItemRangeInserted(lastIndex, diff.size)
        }

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                val searchQuery = breeds.filter {
                    it.main.contains(query as CharSequence, true)
                            || it.sub.contains(query as CharSequence, true)
                }

                breedsList.adapter = BreedAdapter(searchQuery, favorites, ::breedClick, ::addFavorite, ::removeFavorite)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val searchQuery = breeds.filter {
                    it.main.contains(newText as CharSequence, true)
                            || it.sub.contains(newText as CharSequence, true)
                }

                breedsList.adapter = BreedAdapter(searchQuery, favorites, ::breedClick, ::addFavorite, ::removeFavorite)
                return false
            }
        })
    }

    private fun breedClick(breed : Breed)
    {
        val action = BreedsFragmentDirections.actionFragmentBreedsToFragmentBreedsDetail(breed)
        findNavController().navigate(action)
    }

    private fun addFavorite(breed : String, image : String)
    {
        mainViewModel.addFavorite(Favorite(breed, image))
        Snackbar.make(requireView(),
            "${breed.trim()} added to favorites",
            Snackbar.LENGTH_SHORT).show()
    }

    private fun removeFavorite(fav : Favorite)
    {
        mainViewModel.removeFavorite(fav)
        Snackbar.make(requireView(),
            "${fav.breed.trim()} removed from favorites",
            Snackbar.LENGTH_SHORT).show()
    }
}