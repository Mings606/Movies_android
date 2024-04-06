package com.movies.views.movieListing.model

import com.movies.views.movieListing.response.MovieListingsResponse
import java.io.Serializable

class MovieListingsModel : Serializable {

    constructor()

    constructor(item: MovieListingsResponse.Search) {

        item.title?.let { title = it }
        item.poster?.let { poster = it }
        item.imdbID?.let { imdbID = it }
    }

    constructor(title: String = "", poster: String = "", imdbID: String = "") {

        this.title = title
        this.poster = poster
        this.imdbID = imdbID
    }

    var title: String = ""
    var poster: String = ""
    var imdbID: String = ""

}