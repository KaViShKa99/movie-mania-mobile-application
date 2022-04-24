package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MoviesAdapter( private val movies : ArrayList<String>): RecyclerView.Adapter<MoviesAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_card,parent,false)
        return MyViewHolder(view)

    }

    override fun onBindViewHolder(holder: MoviesAdapter.MyViewHolder, position: Int) {
        val movie = movies[position]
        holder.setData(movie)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

           val textView : TextView = itemView.findViewById(R.id.card_title)

           fun setData(hobby : String?){
                textView.text = hobby

           }
    }

}