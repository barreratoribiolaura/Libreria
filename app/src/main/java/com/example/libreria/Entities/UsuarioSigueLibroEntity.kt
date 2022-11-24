package com.example.libreria.Entities

import androidx.room.*

@Entity(tableName = "UsuarioSigueLibroEntity", primaryKeys = ["usuario_id","libro_id"])
data class UsuarioSigueLibroEntity(
    var usuario_id: Long = 0,
    var libro_id: Long = 0
)


