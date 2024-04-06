package com.movies.views.movieListing.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.volley.Request
import com.google.gson.Gson
import com.movies.MyApplication.Companion.database
import com.movies.data.ConstantAPI
import com.movies.data.Movie
import com.movies.data.ResourceAPI
import com.movies.data.StatusAPI
import com.movies.views.movieListing.model.MovieListingsModel
import com.movies.views.movieListing.response.MovieListingsResponse
import com.movies.volley.NetworkManager
import kotlinx.coroutines.launch


class MovieListingsViewModel : ViewModel() {

    var list = ArrayList<MovieListingsModel>()

    var keyWords = "Marvel"

    fun apiMovieListing(): MutableLiveData<ResourceAPI<Any>> {

        val result = MutableLiveData<ResourceAPI<Any>>()

        val url: String = ConstantAPI.apiBaseUrl

        val header = HashMap<String, String>()
        val body = HashMap<String, String>()
        body["type"] = "movie"
        body["s"] = keyWords

        result.value = ResourceAPI.loading(null)
        NetworkManager.instance.callAPI(url, header, body, object : NetworkManager.OnRequestListener {

            override fun onRequest(html: String, repository: ResourceAPI<Any>) {
                try {

                    when (repository.status) {
                        StatusAPI.SUCCESS -> {

                            val response = Gson().fromJson(html, MovieListingsResponse::class.java)

                            list.clear()
                            response?.search?.let {
                                it.forEach {
                                    val model = MovieListingsModel(it)
                                    list.add(model)

                                    viewModelScope.launch {
                                        try {
                                            database.movieDao().insertMovie(Movie(model.imdbID, model.title, model.poster))
                                        } catch (e: Exception) {
                                            e.printStackTrace()
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