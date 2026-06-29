package com.example.geniuscop.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.geniuscop.Partida

@Dao
interface SequenceDao {

    @Insert
    suspend fun inserir(partida: Partida)

    @Query("SELECT * FROM Partida ORDER BY data ASC")
    suspend fun getTodasPartidas(): List<Partida>
}