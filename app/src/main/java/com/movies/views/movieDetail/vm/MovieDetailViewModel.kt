package com.movies.views.movieDetail.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.volley.Request
import com.google.gson.Gson
import com.movies.MyApplication
import com.movies.data.ConstantAPI
import com.movies.data.MovieDetail
import com.movies.data.MovieRatings
import com.movies.data.ResourceAPI
import com.movies.data.StatusAPI
import com.movies.views.movieDetail.model.MovieDetailsModel
import com.movies.views.movieDetail.model.MovieRatingsModel
import com.movies.views.movieDetail.response.MovieDetailResponse
import com.movies.volley.NetworkManager
import kotlinx.coroutines.launch


class MovieDetailViewModel : ViewModel() {

    var list = ArrayList<MovieRatingsModel>()

    var imdbID = ""

    fun apiMovieDetail(): MutableLiveData<ResourceAPI<Any>> {

        val result = MutableLiveData<ResourceAPI<Any>>()

        val url: String = ConstantAPI.apiBaseUrl

        val header = HashMap<String, String>()
        val body = HashMap<String, String>()
        body["i"] = imdbID

        result.value = ResourceAPI.loading(null)
        NetworkManager.instance.callAPI(url, header, body, object : NetworkManager.OnRequestListener {

            override fun onRequest(html: String, repository: ResourceAPI<Any>) {
                try {

                    when (repository.status) {
                        StatusAPI.SUCCESS -> {

                            val response = Gson().fromJson(html, MovieDetailResponse::class.java)

                            response?.let {
                                list.clear()
                                val model = MovieDetailsModel(it)
                                viewModelScope.launch {
                                    try {
                                        MyApplication.database.movieDetailDao().insertMovieDetail(MovieDetail(model.imdbID, model.title, model.year, model.genre, model.plot, model.poster, model.metascore, model.imdbRating, model.imdbVotes))
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }

                                it.ratings?.forEach {
                                    val modelRating = MovieRatingsModel(it)
                                    list.add(modelRating)
                                    viewModelScope.launch {
                                        val rating = MyApplication.database.movieRatingsDao().findRatingByImdbIdAndSource(imdbID, modelRating.source)
                                        if (rating != null) {
                                            MyApplication.database.movieRatingsDao().updateMoviesRatings(imdbID, modelRating.source, modelRating.value)
                                        } else {
                                            MyApplication.database.movieRatingsDao().insertMovieRatings(MovieRatings(0, model.imdbID, modelRating.source, modelRating.value))
                                        }
                                    }
                                }
                            }

                            result.value = ResourceAPI.success(response)
                        }

                        else -> {
                            result.value = repository
                        }
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    result.value = ResourceAPI.error(null)
                }
            }
        }, Request.Method.GET)

        return result
    }
}