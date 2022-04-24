package com.example.myapplication

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.widget.ImageView
import java.io.BufferedInputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class LoadImage(var imageView: ImageView) : AsyncTask<String, Void, Bitmap>() {
    override fun doInBackground(vararg params: String?): Bitmap? {
        var bitmap: Bitmap? = null
        val url = URL(params[0])

        try{
            val con = url.openConnection() as HttpURLConnection
            val bfstream = BufferedInputStream(con.inputStream)
            bitmap = BitmapFactory.decodeStream(bfstream)
        }catch (e : IOException){
            println("image loading error")
        }
        return bitmap
    }

    override fun onPostExecute(bitmap: Bitmap?) {
        imageView.setImageBitmap(bitmap)

    }
}