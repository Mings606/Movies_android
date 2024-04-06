package com.movies.views.movieListing.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.movies.data.BaseResponse

open class MovieListingsResponse : BaseResponse() {

    @SerializedName("Search")
    @Expose
    var search: List<Search>? = null

    @SerializedName("totalResults")
    @Expose
    var totalResults: String? = null

    inner class Search {
        @SerializedName("Title")
        @Expose
        var title: String? = null

        @SerializedName("Year")
        @Expose
        var year: String? = null

        @SerializedName("imdbID")
        @Expose
        var imdbID: String? = null

        @SerializedName("Type")
        @Expose
        var type: String? = null

        @SerializedName("Poster")
        @Expose
        var poster: String? = null
    }

}