package com.movies.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.movies.data.MovieDetail

@Dao
interface MovieDetailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieDetail(movieDetail: MovieDetail)

    @Query("SELECT * FROM movies_detail WHERE imdbID = :imdbID LIMIT 1")
    fun getMovieDetailById(imdbID: String): LiveData<MovieDetail>
}
