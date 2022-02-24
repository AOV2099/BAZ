package com.aov2099.baz.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aov2099.baz.Movie

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovie(movie:Movie)

    @Query("SELECT * FROM movie_table ORDER BY title ASC")
    fun readMovies() : LiveData<List<Movie>>

}