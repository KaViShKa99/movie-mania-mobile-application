package com.example.myapplication

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.w3c.dom.Text
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.reflect.Constructor
import java.net.HttpURLConnection
import java.net.URL

class SearchMovie : AppCompatActivity() {

    var MY_API_KEY : String? = null
    var urlString : String? = null
    var searchBtn : Button? = null
    var saveMovie : Button? = null
    var searchInput : EditText? = null
    var output : TextView? =  null
    var poster : String? =  null
    var image : ImageView? =  null

    lateinit var title : String
    var year : String? =  null
    var rated : String? =  null
    var released : String? =  null
    var runtime : String? =  null
    var genre : String? =  null
    var director : String? =  null
    var writer : String? =  null
    var actors : String? =  null
    var plot : String? =  null



    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_movies)

        searchBtn = findViewById<Button>(R.id.retrieve_movie)
        saveMovie = findViewById<Button>(R.id.save_movie)
        searchInput = findViewById<EditText>(R.id.search_by_movie)
        output = findViewById<TextView>(R.id.movie_details)
        image = findViewById<ImageView>(R.id.movie_image)
        MY_API_KEY = resources.getString(R.string.api_key)


        val db = Room.databaseBuilder(this, AppDatabase::class.java, "mydatabase").build()

        val movieDao = db.movieDao()

        searchBtn?.setOnClickListener{
            getMovie(searchInput!!.text.toString())
        }
        saveMovie?.setOnClickListener {
            runBlocking {
                launch {

                    if (searchInput!!.text.toString() != "" ){
                        movieDao.insertMovies(
                            Movie(
                                title,
                                year,
                                rated,
                                released,
                                runtime,
                                genre,
                                director,
                                writer,
                                actors,
                                plot
                            )
                        )

                        Toast.makeText(applicationContext, "Saved...!", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(applicationContext, "Invalid Input", Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        }
    }


    fun getMovie(movieName : String){
        println(movieName)
        urlString = "https://www.omdbapi.com/?t=$movieName&apikey=$MY_API_KEY"
        var  data: String = ""


        runBlocking {
            withContext(Dispatchers.IO){
                val stb = StringBuilder("")

                val url = URL(urlString)
                val con = url.openConnection() as HttpURLConnection
                val bf: BufferedReader
                try {
                    bf = BufferedReader(InputStreamReader(con.inputStream))
                }
                catch (e: IOException) {
                    e.printStackTrace()
                    return@withContext
                }

                var line = bf.readLine()
                while (line != null) {
                    stb.append(line)
                    line = bf.readLine()
                }

                val json = JSONObject(stb.toString())
                val response  =  json["Response"].toString()

                if (response == "True"){ //check movie is here or not
                    data = parseJson(stb)
                    output!!.text = data
                }else{
                    output!!.text = "Movie Not Found !"
                    var newMovieImage = LoadImage(image!!)
                    newMovieImage.execute("https://i.postimg.cc/QxFG4wvw/No-Image-Found.png") // set default image


                }

            }
        }
    }

    fun parseJson(stb: StringBuilder): String{



        val json = JSONObject(stb.toString())

        title = json["Title"].toString()
        year = json["Year"].toString()
        rated = json["Rated"].toString()
        released = json["Released"].toString()
        runtime = json["Runtime"].toString()
        genre = json["Genre"].toString()
        director = json["Director"].toString()
        writer = json["Writer"].toString()
        actors = json["Actors"].toString()
        plot = json["Plot"].toString()
        poster = json["Poster"].toString()
        println(poster)
        if(poster != "N/A"){
            var newMovieImage = LoadImage(image!!)
            newMovieImage.execute(poster)
        }else{
            var newMovieImage = LoadImage(image!!)
            newMovieImage.execute("https://i.postimg.cc/QxFG4wvw/No-Image-Found.png")
        }


            return "Title :   " + title + "\n" +
                            "Year :   " + year + "\n" +
                            "Rated :   " + rated + "\n" +
                            "Released :   " + released + "\n" +
                            "Runtime :   " + runtime + "\n" +
                            "Genre :   " + genre + "\n" +
                            "Director :   " + director + "\n" +
                            "Writer :   " + writer + "\n" +
                            "Actors :   " + actors + "\n" +
                            "Plot :   " +"\n\n"+"\"\" "+ plot +" \"\""+ "\n"

    }

}