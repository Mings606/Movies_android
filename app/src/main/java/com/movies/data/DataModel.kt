package com.movies.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey @ColumnInfo(name = "imdb_id") val imdbID: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "poster_url") val poster: String
)

@Entity(tableName = "movies_detail")
data class MovieDetail(
    @PrimaryKey @ColumnInfo(name = "imdbID") val imdbID: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "year") val year: String,
    @ColumnInfo(name = "genre") val genre: String,
    @ColumnInfo(name = "plot") val plot: String,
    @ColumnInfo(name = "poster") val poster: String,
    @ColumnInfo(name = "metascore") val metascore: String,
    @ColumnInfo(name = "imdbRating") val imdbRating: String,
    @ColumnInfo(name = "imdbVotes") val imdbVotes: String
)

@Entity(tableName = "movie_ratings")
data class MovieRatings(
    @PrimaryKey(autoGenerate = true) val ratingID: Int,
    @ColumnInfo(name = "imdb_id") val imdbID: String,
    @ColumnInfo(name = "source") val source: String,
    @ColumnInfo(name = "value") val value: String
)