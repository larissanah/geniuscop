package com.example.geniuscop.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.geniuscop.Partida

@Dao
interface PartidaDao {
    @Insert
    suspend fun inserir(partida: Partida)

    @Query("SELECT * FROM Partida ORDER BY data ASC")
    suspend fun getTodasPartidas(): List<Partida>
}