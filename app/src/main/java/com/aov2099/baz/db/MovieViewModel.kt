package com.aov2099.baz.db

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.aov2099.baz.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieViewModel(application:Application): AndroidViewModel(application) {

  val readData:LiveData<List<Movie>>
  private val repository: MovieRepository

  init {
    val movieDao = MovieDB.getDatabase(application).movieDao()
    repository = MovieRepository(movieDao)
    readData = repository.readAllData
  }

  fun addMovie(movie:Movie){
    viewModelScope.launch {
      Dispatchers.IO
      repository.addMovie(movie)
    }
  }


}