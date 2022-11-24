package com.example.libreria.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "LibroEntity")
data class LibroEntity(
    @PrimaryKey(autoGenerate = true)
    var libro_id: Long = 0,
    var titulo: String,
    var portada: String,
    var autor: String,
    var resumen: String,
    var enlace: String,
    var creado: String,
    var npapginas: String,
)
