package com.movies.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.movies.dao.MovieDao
import com.movies.dao.MovieDetailDao
import com.movies.dao.MovieRatingsDao

@Database(entities = [Movie::class, MovieDetail::class, MovieRatings::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun movieDetailDao(): MovieDetailDao
    abstract fun movieRatingsDao(): MovieRatingsDao
}