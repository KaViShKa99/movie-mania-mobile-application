package com.example.myapplication


import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class HomePage: AppCompatActivity() {


    override fun onCreate(savedInstanceState : Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_page)
//        supportActionBar?.hide()

        val searchMovies = findViewById<Button>(R.id.search_movies)
        val addMovie = findViewById<Button>(R.id.add_movie)
        val searchActors = findViewById<Button>(R.id.search_actors)
        val searchMovieList = findViewById<Button>(R.id.get_all_movies)

        searchMovies.setOnClickListener{
            val searchMovieIntent = Intent(this,SearchMovie :: class.java)
            startActivity(searchMovieIntent)
        }

        addMovie.setOnClickListener {
            val addMovieIntent = Intent(this,AddMovie :: class.java)
            startActivity(addMovieIntent)
        }

        searchActors.setOnClickListener {
            val searchActorsIntent = Intent(this,SearchActors :: class.java)
            startActivity(searchActorsIntent)
        }
        searchMovieList.setOnClickListener {
            val searchMovieListIntent = Intent(this,SearchAllMovies :: class.java)
            startActivity(searchMovieListIntent)
        }

    }
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        }
    }
}