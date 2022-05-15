package com.g.tragosapp.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.DiffResult.NO_POSITION
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.g.tragosapp.R
import com.g.tragosapp.core.BaseViewHolder
import com.g.tragosapp.data.model.Cocktail
import com.g.tragosapp.databinding.TragosRowBinding



class MainAdapter(
    private val context: Context,
    private val itemClickListener: OnTragoClickListener


) : RecyclerView.Adapter<BaseViewHolder<*>>() {
    private var isCocktailFavorited: Boolean? = null
    private var cocktailList = listOf<Cocktail>()

    interface OnTragoClickListener {
        fun onCocktailClick(cocktail: Cocktail, position: Int)
    }

    fun setCocktailList(cocktailList: List<Cocktail>) {
        this.cocktailList = cocktailList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val itemBinding = TragosRowBinding.inflate(LayoutInflater.from(context), parent, false)

        val holder = MainViewHolder(itemBinding)

        itemBinding.imgStar.setOnClickListener {
            val position = holder.adapterPosition.takeIf { it != NO_POSITION } ?: return@setOnClickListener
            cocktailList[position].isFav = !cocktailList[position].isFav

            itemClickListener.onCocktailClick(cocktailList[position], position)
        }

        return holder
    }

    override fun getItemCount(): Int = cocktailList.size

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is MainViewHolder -> holder.bind(cocktailList[position])
        }
    }

    private inner class MainViewHolder(
        val binding: TragosRowBinding
    ) : BaseViewHolder<Cocktail>(binding.root) {
        override fun bind(item: Cocktail) = with(binding) {

          if (item.isFav)
          {
              imgStar.setImageResource(R.drawable.ic_baseline_star_24)

          }
            else{
              imgStar.setImageResource(R.drawable.ic_baseline_star_outline_24)
            }


            Glide.with(context)
                .load(item.image)
                .centerCrop()
                .into(imgCocktail)

            txtTitulo.text = item.name
            txtAlcholic.text = item.hasAlcohol
            txtDescripcion.text = item.description
        }
    }
}