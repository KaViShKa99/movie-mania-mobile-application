package com.example.myapplication

import androidx.room.Entity


@Entity
data class ActorInfo(
    val actorName: String?,
    val movies: MutableList<String?>
)