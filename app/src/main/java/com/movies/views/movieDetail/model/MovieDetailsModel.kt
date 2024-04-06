package com.movies.views.movieDetail.model

import com.movies.views.movieDetail.response.MovieDetailResponse
import java.io.Serializable

class MovieDetailsModel : Serializable {

    constructor()

    constructor(item: MovieDetailResponse) {

        item.imdbID?.let { imdbID = it }
        item.title?.let { title = it }
        item.year?.let { year = it }
        item.genre?.let { genre = it }
        item.plot?.let { plot = it }
        item.poster?.let { poster = it }
        item.metascore?.let { metascore = it }
        item.imdbRating?.let { imdbRating = it }
        item.imdbVotes?.let { imdbVotes = it }

    }

    var imdbID: String = ""
    var title: String = ""
    var year: String = ""
    var genre: String = ""
    var plot: String = ""
    var poster: String = ""
    var metascore: String = ""
    var imdbRating: String = ""
    var imdbVotes: String = ""

}