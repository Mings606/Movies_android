package com.movies.views.movieListing

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.movies.MyApplication
import com.movies.R
import com.movies.data.StatusAPI
import com.movies.databinding.ActivityMoviesListingBinding
import com.movies.views.movieDetail.MovieDetailActivity
import com.movies.views.movieListing.adapter.MovieListingsAdapter
import com.movies.views.movieListing.model.MovieListingsModel
import com.movies.views.movieListing.vm.MovieListingsViewModel
import kotlinx.coroutines.launch

class MoviesListingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMoviesListingBinding
    private lateinit var movieListingsViewModel: MovieListingsViewModel
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.black)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movies_listing)
        movieListingsViewModel = ViewModelProvider(this)[MovieListingsViewModel::class.java]

        bindingView()

        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
                val isWifi = networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: false
                val isCellular = networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ?: false

                runOnUiThread {
                    when {
                        isWifi -> {
                            apiMovieListing()
                        }

                        isCellular -> {
                            apiMovieListing()
                        }

                        else -> {
                            dataList()
                        }
                    }
                }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                runOnUiThread {
                    dataList()
                }
            }
        }

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

    }

    private fun bindingView() {

        binding.etSearch.requestFocus()

        binding.etSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                Toast.makeText(this, "Searching...", Toast.LENGTH_SHORT).show()
                movieListingsViewModel.keyWords = binding.etSearch.text.toString()
                apiMovieListing()

                true
            } else {
                false
            }
        }

    }

    private fun apiMovieListing() {

        movieListingsViewModel.apiMovieListing().observe(this) {

            when (it?.status) {

                StatusAPI.SUCCESS -> {
                    dataList()
                }

                else -> {
                    dataList()
                }
            }
        }
    }

    private fun dataList() {
        lifecycleScope.launch {
            MyApplication.database.movieDao().getSearchMoviesTitle(movieListingsViewModel.keyWords).observe(this@MoviesListingActivity) {
                movieListingsViewModel.list.clear()
                it.forEach {
                    movieListingsViewModel.list.add(MovieListingsModel(it.title, it.poster, it.imdbID))
                }
                binding.rvMovies.adapter = null
                binding.rvMovies.adapter?.notifyDataSetChanged()
                droidList()
            }
        }
    }

    private fun droidList() {

        runOnUiThread {

            if (binding.rvMovies.adapter == null) {

                binding.rvMovies.adapter =
                    MovieListingsAdapter(this, movieListingsViewModel.list,
                        object : MovieListingsAdapter.OnItemClickListener {

                            override fun onItemClick(item: MovieListingsModel) {
                                val intent = Intent(this@MoviesListingActivity, MovieDetailActivity::class.java)
                                val bundle = Bundle()
                                bundle.putString("imdbID", item.imdbID)
                                intent.putExtras(bundle)
                                startActivity(intent)
                            }
                        })
            } else {
                Handler().postDelayed({
                    binding.rvMovies.adapter?.notifyDataSetChanged()
                }, 100)
            }
        }
    }

}