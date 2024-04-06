package com.movies.views.movieDetail.model

import com.movies.views.movieDetail.response.MovieDetailResponse
import java.io.Serializable

class MovieRatingsModel : Serializable {

    constructor()

    constructor(item: MovieDetailResponse.Rating) {

        item.source?.let { source = it }
        item.value?.let { value = it }

    }

    constructor(imdbID: String, source: String, value: String) {

        this.imdbID = imdbID
        this.source = source
        this.value = value

    }

    var imdbID: String = ""
    var source: String = ""
    var value: String = ""

}