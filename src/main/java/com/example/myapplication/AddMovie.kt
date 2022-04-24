package com.example.myapplication

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.regex.Matcher
import java.util.regex.Pattern



class AddMovie: AppCompatActivity() {

    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_movie)


        val title = findViewById<EditText>(R.id.set_title)
        val year = findViewById<EditText>(R.id.set_year)
        val rated = findViewById<EditText>(R.id.set_rated)
        val released = findViewById<EditText>(R.id.set_released)
        val runtime = findViewById<EditText>(R.id.set_runtime)
        val genre = findViewById<EditText>(R.id.set_genre)
        val director = findViewById<EditText>(R.id.set_director)
        val writer = findViewById<EditText>(R.id.set_writer)
        val actors = findViewById<EditText>(R.id.set_actors)
        val plot = findViewById<EditText>(R.id.set_plot)
        val addBtn = findViewById<Button>(R.id.add_movie_details)
        val deleteBtn = findViewById<Button>(R.id.delete_movie_details)
        val deleteKey = findViewById<EditText>(R.id.delete_keyword)



        val db = Room.databaseBuilder(this, AppDatabase::class.java, "mydatabase").build()

        val movieDao = db.movieDao()


        newRecyclerView =  findViewById(R.id.recyclerView)
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)
        newArrayList = arrayListOf<String>()

        deleteBtn.setOnClickListener {
            runBlocking {
                launch {
                    movieDao.deleteByTitle(deleteKey.text.toString())

                    //activity refresh
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);

                    deleteKey.text = null
                }
            }
        }

  //loading all movies
        runBlocking {
            launch {


                val movies: List<Movie> = movieDao.getAll()

                var outputFormat:String? =  null

                for (movie in movies){
                    outputFormat = "Title :   " + movie.title + "\n" +
                            "Year :   " + movie.year + "\n" +
                            "Rated :   " + movie.rated + "\n" +
                            "Released :   " + movie.released + "\n" +
                            "Genre :   " + movie.genre + "\n" +
                            "Director :   " + movie.director + "\n" +
                            "Writer :   " + movie.writer + "\n" +
                            "Actors :   " + movie.actors + "\n" +
                            "Plot :   " +"\n\n"+"\"\" "+ movie.plot +" \"\""+ "\n"
                    newArrayList.add(outputFormat)
                }
                newRecyclerView.adapter = MoviesAdapter(newArrayList)
            }
        }


        addBtn.setOnClickListener {
            runBlocking {
                launch {



                    if ( title.text.toString() != "" && year.text.toString() != "" && rated.text.toString() != ""
                        && released.text.toString() != "" && runtime.text.toString() != "" && genre.text.toString() != ""
                        && director.text.toString() != "" && writer.text.toString() != "" && actors.text.toString() != ""
                        && plot.text.toString() != "" )
                        {


                                movieDao.insertMovies(
                                    Movie(
                                        title.text.toString(),
                                        year.text.toString(),
                                        rated.text.toString(),
                                        released.text.toString(),
                                        runtime.text.toString(),
                                        genre.text.toString(),
                                        director.text.toString(),
                                        writer.text.toString(),
                                        actors.text.toString(),
                                        plot.text.toString()
                                    )
                                )
                            Toast.makeText(applicationContext, "saved...!", Toast.LENGTH_SHORT).show()

                                    val movies: List<Movie> = movieDao.getAll()

                                    var outputFormat:String? =  null

                                    for (movie in movies){
                                        outputFormat = "Title :   " + movie.title + "\n" +
                                                "Year :   " + movie.year + "\n" +
                                                "Rated :   " + movie.rated + "\n" +
                                                "Released :   " + movie.released + "\n" +
                                                "Genre :   " + movie.genre + "\n" +
                                                "Director :   " + movie.director + "\n" +
                                                "Writer :   " + movie.writer + "\n" +
                                                "Actors :   " + movie.actors + "\n" +
                                                "Plot :   " +"\n\n"+"\"\" "+ movie.plot +" \"\""+ "\n"
                                        newArrayList.add(outputFormat!!)
                                    }
                                    newRecyclerView.adapter = MoviesAdapter(newArrayList)

                    }else{
                        Toast.makeText(applicationContext, "please fill all field", Toast.LENGTH_SHORT).show()
                    }

                    //set edit text as null
                    title.text = null
                    year.text = null
                    rated.text = null
                    released.text = null
                    runtime.text = null
                    genre.text = null
                    director.text = null
                    writer.text = null
                    actors.text = null
                    plot.text = null

                }
            }
        }
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN //hide status bar
        }
    }

}