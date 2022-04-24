package com.example.myapplication

import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SearchActors: AppCompatActivity() {

    var searchActorName :String? = null
    private var actor : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_actors)

        val result = findViewById<TextView>(R.id.results)
        val searchByActor = findViewById<EditText>(R.id.search_by_actors)
        val searchBtn = findViewById<Button>(R.id.search_btn)

        val db = Room.databaseBuilder(this, AppDatabase::class.java, "mydatabase").build()

        val movieDao = db.movieDao()

        searchBtn.setOnClickListener {
            result.text = null

            actor  = searchByActor.text.toString()




            runBlocking {
                launch {

                    val movies: List<Movie> = movieDao.getAll()
                    var actors: List<String>? = null
                    val names: List<TitleAndActors> = movieDao.searchName(actor)
                    val arrayList = ArrayList<ActorInfo>()


                    val regex = """(?i)($actor)""".toRegex()


                    for (k in names) {
                        actors = k.actors?.split(",")
                        if (actors != null) {
                            for (i in actors) {
                                if (regex.containsMatchIn(i)) {

                                    if (arrayList.size != 0) {

                                        for (index in arrayList.indices - 1) {
                                            if (i == arrayList[index].actorName) {
                                                arrayList[index].movies.add(" ${k.title} ")
                                            } else {
                                                arrayList.add(
                                                    ActorInfo(
                                                        i,
                                                        mutableListOf(" ${k.title} ")
                                                    )
                                                )
                                            }
                                        }
                                    } else {
                                        arrayList.add(ActorInfo(i, mutableListOf(" ${k.title} ")))
                                    }
                                }
                            }
                        }

                    }

                    if(arrayList.size != 0) {
                        for (l in arrayList.indices) {
                            result.append(arrayList[l].actorName + " == " + arrayList[l].movies + "\n\n")
                        }
                    }else{
                        result.append("Not Found")
                    }

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