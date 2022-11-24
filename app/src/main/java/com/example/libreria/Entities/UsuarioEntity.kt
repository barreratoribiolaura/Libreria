package com.example.libreria.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UsuarioEntity")
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true)
    var usuario_id: Long = 0,
    var nombre: String,
    var contrasenya: String,
)
