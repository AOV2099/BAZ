package com.aov2099.baz

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface APIService {

    /*@GET
    suspend fun getMoviesByPopularity( ): Response<MovieResponse>*/
    @GET
    suspend fun getMoviesByTitle( @Url URL: String): Response<MovieResponse>


}