package com.aov2099.baz.fragments

import android.annotation.SuppressLint
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aov2099.baz.APIService
import com.aov2099.baz.Movie
import com.aov2099.baz.MovieAdapter
import com.aov2099.baz.Network
import com.aov2099.baz.databinding.ActivityMainBinding
import com.aov2099.baz.db.MovieViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


class SearchFragment : Fragment(), SearchView.OnQueryTextListener,
    android.widget.SearchView.OnQueryTextListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MovieAdapter
    private val moviesData = mutableListOf<Movie>()
    lateinit var movieViewModel: MovieViewModel
    var context = this
    var connectivity: ConnectivityManager? = null
    var info: NetworkInfo? = null


    val rvMovies: RecyclerView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_search, container, false)

        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.svMovies.setOnQueryTextListener(this)
        movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)
        //setContentView(binding.root)


        setUp() //init RV

        if (Network.conExists( requireActivity() ) ) {
            loadMovies()
        } else {
            loadDataFromDB()
        }

        return binding.root
    }

    private fun setUp() { //

        adapter = MovieAdapter(moviesData)
        binding.rvMovies.layoutManager = LinearLayoutManager(parentFragment?.context)
        binding.rvMovies.adapter = adapter
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onQueryTextSubmit(query: String?): Boolean {
        if (!query.isNullOrEmpty()) {
            if ( Network.conExists( requireActivity() ) ) {
                searchMovieByName(query.lowercase(Locale.getDefault()))
            } else {
                loadDataFromDB()
            }

        }

        return true
    }

    private fun loadDataFromDB() {
        moviesData.clear()

        Toast.makeText(
            this@SearchFragment.activity,
            "Conexión perdida, cargando datos guardados",
            Toast.LENGTH_LONG
        ).show()

        val movies: List<Movie> = emptyList()

        movieViewModel.readData.observe(viewLifecycleOwner) { movie ->
            moviesData.addAll(movie)
            adapter.notifyDataSetChanged()
        }


    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }


    private fun getRetrofit(): Retrofit { //retrieve call
        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    private fun Fragment?.runOnUiThread(action: () -> Unit) {
        this ?: return
        if (!isAdded) return
        activity?.runOnUiThread(action)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun searchMovieByName(title: String) { // coroutine that search movie in another threat
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(APIService::class.java)
                .getMoviesByTitle("search/movie?api_key=2c511359a1247bb16caee6cf2dc1bd43&query=$title")

            val res = call.body()?.results

            runOnUiThread {

                if (call.isSuccessful) {

                    val movies: List<Movie> = res ?: emptyList()

                    if (movies.size > 1) {
                        moviesData.clear()
                        moviesData.addAll(movies)
                        insertDataIntoDB(movies)
                        adapter.notifyDataSetChanged()

                    } else {
                        Toast.makeText(
                            this@SearchFragment.activity,
                            "No se han encontrado coincidencias",
                            Toast.LENGTH_LONG
                        ).show()
                        loadMovies()
                    }

                } else {
                    //error
                    Toast.makeText(
                        this@SearchFragment.activity,
                        "Error al obetener data",
                        Toast.LENGTH_LONG
                    ).show()
                }


            }

        }


    }


    private fun loadMovies() { // load popular movies
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(APIService::class.java)
                .getMoviesByTitle("movie/popular?api_key=2c511359a1247bb16caee6cf2dc1bd43")

            val res = call.body()?.results

            //TODO("Making the key a constant")

            runOnUiThread {

                if (call.isSuccessful) {

                    val movies: List<Movie> = res ?: emptyList()
                    moviesData.clear()
                    moviesData.addAll(movies)
                    insertDataIntoDB(movies)
                    adapter.notifyDataSetChanged()

                } else {
                    //error
                    Toast.makeText(
                        this@SearchFragment.requireActivity(),
                        "Error, revisa tu entrada de datos",
                        Toast.LENGTH_LONG
                    ).show()

                }

                //onBackPressed()

            }

        }
    }


    private fun insertDataIntoDB(movies: List<Movie>) {

        for (movie in movies) {
            val newMovie = Movie(movie.id, movie.title, movie.overview, movie.poster ?: "")

            movieViewModel.addMovie(newMovie)
            println("Película agregada correctamente")
        }

    }



}