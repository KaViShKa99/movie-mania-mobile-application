package com.example.myapplication

import androidx.room.Entity

@Entity
data class TitleAndActors(
    val actors : String?,
    val title : String?,

)
