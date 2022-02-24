package com.aov2099.baz

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.aov2099.baz.databinding.ItemMovieBinding
import com.squareup.picasso.Picasso

class MovieViewHolder ( view:View ) : RecyclerView.ViewHolder( view ){

    private val binding = ItemMovieBinding.bind(view)

    fun bind(movie: Movie) {

        Picasso.get().load("https://image.tmdb.org/t/p/w500${movie.poster}").into(binding.ivMovie)
        binding.tvTitle.text = movie.title
        binding.tvOverview.text = movie.overview
    }

}