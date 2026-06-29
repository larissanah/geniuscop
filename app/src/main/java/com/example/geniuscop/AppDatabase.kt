package com.example.geniuscop

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.geniuscop.database.SequenceDao

@Database(
    entities = [Partida::class], // lista de entidades (tabelas)
    version = 1,                 // versão do banco
    exportSchema = false         // evita gerar arquivos de esquema
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sequenceDao(): SequenceDao
}