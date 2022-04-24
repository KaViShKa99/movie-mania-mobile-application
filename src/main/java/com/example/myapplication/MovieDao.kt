package com.example.myapplication

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieDao {
    @Query("Select * from movie")
    suspend fun  getAll(): List<Movie>

    @Query("SELECT actors,title FROM movie WHERE actors LIKE '%' || :search || '%'")
    suspend fun searchName(search: String?): List<TitleAndActors>

    @Query("DELETE FROM movie WHERE title = :title")
    suspend fun deleteByTitle(title: String?)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovies(vararg movie: Movie)

    @Insert
    suspend fun insertAll(vararg movies: Movie)
}