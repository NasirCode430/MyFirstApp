package com.g.tragosapp.ui

import android.annotation.SuppressLint
import android.app.ActionBar
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.g.tragosapp.R
import com.g.tragosapp.application.MainActivity
import com.g.tragosapp.core.Resource
import com.g.tragosapp.data.model.Cocktail
import com.g.tragosapp.databinding.ActivityStartBinding
import com.g.tragosapp.databinding.FragmentMainBinding
import com.g.tragosapp.presentation.MainViewModel
import com.g.tragosapp.utils.*
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main),
    MainAdapter.OnTragoClickListener {
    private val viewModel by activityViewModels<MainViewModel>()
    private lateinit var mainAdapter: MainAdapter
    private lateinit var binding:FragmentMainBinding
    private lateinit var binding2:ActivityStartBinding


    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (activity as MainActivity?)?.getSupportActionBar()?.setTitle(R.string.title_home)
        val bar: ActionBar? = activity?.getActionBar()
        bar?.setBackgroundDrawable(ColorDrawable(R.color.grey))
//        binding.toolbar.setBackgroundColor(R.color.grey)
//        binding.toolbar.setTitle("Drink Recipies")
        mainAdapter = MainAdapter(requireContext(), this)
    }

   private  fun radioClick () {
         viewModel.setSearchByName(true)
         viewModel.setCocktail("margarita")

         binding.radioName.setOnClickListener {
             viewModel.setSearchByName(true)
             viewModel.setCocktail("margarita")
         }
         binding.radioAlpha.setOnClickListener {
             viewModel.setSearchByName(false)
             viewModel.setCocktail("a")
         }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         binding = FragmentMainBinding.bind(view)

        binding.rvTragos.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTragos.adapter = mainAdapter
        radioClick()
        binding.searchView.onQueryTextChanged {
            viewModel.setCocktail(it)
        }
        viewModel.fetchCocktailList.observe(viewLifecycleOwner, Observer { result ->
            binding.progressBar.showIf { result is Resource.Loading }

            when (result) {
                is Resource.Loading -> {
                    binding.emptyContainer.root.hide()
                }
                is Resource.Success -> {
                    if (result.data.isEmpty()) {
                        binding.rvTragos.hide()
                        binding.emptyContainer.root.show()
                        return@Observer
                    }
                    binding.rvTragos.show()
//                    result.data.forEach {
//                        it.isFav = viewModel.isCocktailFavorite(it)
//                    }
                    mainAdapter.setCocktailList(result.data)
                    binding.emptyContainer.root.hide()
                }
                is Resource.Failure -> {
                    showToast("Error  ${result.exception}")
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.favoritos -> {
                findNavController().navigate(R.id.action_mainFragment_to_favoritosFragment)
                false
            }
            else -> false
        }
    }

    override fun onCocktailClick(cocktail: Cocktail, position: Int) {
//viewModel.isCocktailFavorite(cocktail)
        viewModel.saveOrDeleteFavoriteCocktail(cocktail)
        mainAdapter.notifyDataSetChanged()
//        findNavController().navigate(
//            MainFragmentDirections.actionMainFragmentToTragosDetalleFragment(
//                cocktail
//            )
//        )
    }
}


