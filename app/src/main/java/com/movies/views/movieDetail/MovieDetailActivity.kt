package com.movies.views.movieDetail

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.movies.MyApplication
import com.movies.R
import com.movies.data.StatusAPI
import com.movies.databinding.ActivityMovieDetailBinding
import com.movies.views.movieDetail.adapter.MovieRatingsAdapter
import com.movies.views.movieDetail.model.MovieRatingsModel
import com.movies.views.movieDetail.vm.MovieDetailViewModel
import kotlinx.coroutines.launch

class MovieDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMovieDetailBinding
    private lateinit var movieDetailViewModel: MovieDetailViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.black)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail)
        movieDetailViewModel = ViewModelProvider(this)[MovieDetailViewModel::class.java]

        intent.extras?.let {
            movieDetailViewModel.imdbID = it.getString("imdbID").toString()
        }

        bindingView()
        apiMovieDetails()
    }

    private fun bindingView() {

        binding.btnBack.setOnClickListener {
            finish()
        }

    }

    private fun apiMovieDetails() {

        movieDetailViewModel.apiMovieDetail().observe(this) {

            when (it?.status) {

                StatusAPI.SUCCESS, StatusAPI.FAILED -> {
                    getData()
                }

                else -> {
                    getData()
                }
            }
        }
    }

    private fun getData() {
        lifecycleScope.launch {

            MyApplication.database.movieDetailDao().getMovieDetailById(movieDetailViewModel.imdbID).observe(this@MovieDetailActivity) { movieDetail ->
                if (movieDetail != null) {
                    try {
                        binding.ratingBar.rating = (movieDetail.metascore.toFloat() / 100) * 5
                    } catch (e: Exception) {
                        binding.ratingBar.rating = 0f
                        e.printStackTrace()
                    }

                    Glide.with(this@MovieDetailActivity).load(movieDetail.poster).into(binding.imgPoster)
                    Glide.with(this@MovieDetailActivity).load(movieDetail.poster).into(binding.imgBgPoster)

                    binding.tvTitle.text = "${movieDetail.title} (${movieDetail.year})"
                    binding.tvRatings.text = "${movieDetail.imdbRating} / 10"
                    binding.tvVoting.text = "${movieDetail.imdbVotes} Ratings"
                    binding.tvDescription.text = movieDetail.genre
                    binding.tvPlotSummary.text = movieDetail.plot
                }
            }

            MyApplication.database.movieRatingsDao().getMoviesRatings(movieDetailViewModel.imdbID).observe(this@MovieDetailActivity) { ratings ->
                if (ratings != null) {
                    movieDetailViewModel.list.clear()
                    ratings.forEach {
                        movieDetailViewModel.list.add(MovieRatingsModel(it.imdbID, it.source, it.value))
                    }
                    droidList()
                }
            }
        }
    }

    private fun droidList() {

        runOnUiThread {

            if (binding.rvReviews.adapter == null) {

                binding.rvReviews.adapter =
                    MovieRatingsAdapter(movieDetailViewModel.list)
            } else {
                Handler().postDelayed({
                    binding.rvReviews.adapter?.notifyDataSetChanged()
                }, 100)
            }
        }
    }
}