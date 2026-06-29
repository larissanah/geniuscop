package com.example.geniuscop


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "partida")
data class Partida(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val acertos: Int,
    val data: Long
)