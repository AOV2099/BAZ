package com.aov2099.baz
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class MovieResponse (

    @SerializedName("page") var page:String,
    @SerializedName("results") var results:List<Movie>

)

@Entity( tableName = "movie_table")
data class Movie (
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id") var id:Int,
    @SerializedName("title") var title:String,
    @SerializedName("overview") var overview:String,
    @SerializedName("poster_path") var poster:String

)