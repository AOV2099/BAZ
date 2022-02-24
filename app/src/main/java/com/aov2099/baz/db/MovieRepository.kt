package com.aov2099.baz.db

import androidx.lifecycle.LiveData
import com.aov2099.baz.Movie

class MovieRepository( private val movieDao:MovieDao) {
    val readAllData: LiveData<List<Movie>> = movieDao.readMovies()

    suspend fun addMovie( movie:Movie ){
        movieDao.addMovie( movie )
    }
}