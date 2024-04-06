package com.movies.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.movies.data.MovieRatings

@Dao
interface MovieRatingsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieRatings(movieRatings: MovieRatings)

    @Query("SELECT * FROM movie_ratings WHERE imdb_id = :imdbID")
    fun getMoviesRatings(imdbID: String): LiveData<List<MovieRatings>>

    @Query("UPDATE movie_ratings SET value = :value WHERE imdb_id = :imdbID AND source = :source")
    suspend fun updateMoviesRatings(imdbID: String, source: String, value: String)

    @Query("SELECT * FROM movie_ratings WHERE imdb_id = :imdbID AND source = :source LIMIT 1")
    suspend fun findRatingByImdbIdAndSource(imdbID: String, source: String): MovieRatings?
}
