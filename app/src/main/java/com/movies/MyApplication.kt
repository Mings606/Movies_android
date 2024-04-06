package com.movies

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.google.firebase.FirebaseApp
import com.movies.data.MovieDatabase

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)

        contextApp = applicationContext

        database = Room.databaseBuilder(this, MovieDatabase::class.java, "movie_list_database").build()
    }

    companion object {
        lateinit var contextApp: Context
        lateinit var database: MovieDatabase
    }

}