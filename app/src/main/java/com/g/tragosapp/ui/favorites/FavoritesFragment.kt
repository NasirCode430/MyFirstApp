package com.g.tragosapp.ui.favorites

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.g.tragosapp.R
import com.g.tragosapp.application.MainActivity
import com.g.tragosapp.core.Resource
import com.g.tragosapp.data.model.Cocktail
import com.g.tragosapp.databinding.ActivityStartBinding
import com.g.tragosapp.databinding.FavoriteFragmentBinding
import com.g.tragosapp.presentation.MainViewModel
import com.g.tragosapp.utils.show
import com.g.tragosapp.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment(R.layout.favorite_fragment),
    FavoritesAdapter.OnCocktailClickListener {
    private val viewModel by activityViewModels<MainViewModel>()
    private lateinit var favoritesAdapter: FavoritesAdapter
    lateinit var binding3: ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoritesAdapter = FavoritesAdapter(requireContext(), this)
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FavoriteFragmentBinding.bind(view)
        (activity as MainActivity?)?.getSupportActionBar()?.setTitle(R.string.title_dashboard)

//        binding.toolbar.setBackgroundColor(R.color.grey)
//        binding.toolbar.setTitle("Favorites Recipies")
        binding.rvTragosFavoritos.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTragosFavoritos.adapter = favoritesAdapter

        viewModel.getFavoriteCocktails().observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    if (result.data.isEmpty()) {
                        binding.emptyContainer.root.show()
                        return@Observer
                    }
                    favoritesAdapter.setCocktailList(result.data)
                }
                is Resource.Failure -> {
                    showToast("An error occurred ${result.exception}")
                }
            }
        })
    }

    override fun onCocktailClick(cocktail: Cocktail, position: Int) {
//        findNavController().navigate(
//            FavoritesFragmentDirections.actionFavoritosFragmentToTragosDetalleFragment(
//                cocktail
//            )
//        )
    }

    override fun onCocktailLongClick(cocktail: Cocktail, position: Int) {
        viewModel.deleteFavoriteCocktail(cocktail)
    }
}