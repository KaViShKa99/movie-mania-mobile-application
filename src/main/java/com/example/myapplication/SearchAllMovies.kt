package com.example.myapplication

import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class SearchAllMovies : AppCompatActivity() {

    private var MY_API_KEY : String? = null
    private var resultsMovies: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_all_movies)

        val searchKeyword = findViewById<EditText>(R.id.search_keyword)
        val searchAllMoviesBtn = findViewById<Button>(R.id.search_all_movies_btn)
        resultsMovies= findViewById<TextView>(R.id.all_movies_results)
        MY_API_KEY = resources.getString(R.string.api_key)


        searchAllMoviesBtn.setOnClickListener {


                getAllMovies(searchKeyword.text.toString())

                searchKeyword.text =  null



        }

    }

    fun getAllMovies(movieName :String){
        val urlString = "https://www.omdbapi.com/?s=$movieName&apikey=$MY_API_KEY"

        var  data: String = ""


        runBlocking {
            withContext(Dispatchers.IO) {
                val stb = StringBuilder("")

                val url = URL(urlString)
                val con = url.openConnection() as HttpURLConnection
                val bf: BufferedReader
                try {
                    bf = BufferedReader(InputStreamReader(con.inputStream))
                } catch (e: IOException) {
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
//                println(response)
                if (response == "True") {
                    val newJsonArray = json.getJSONArray("Search")


                    for (index in 0 until newJsonArray.length()) {
                        val newFilmArray = JSONObject(newJsonArray[index].toString())

                        resultsMovies?.text = newFilmArray["Title"].toString() + "\n"
                    }
                }else{
                    resultsMovies?.text = "not found"
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