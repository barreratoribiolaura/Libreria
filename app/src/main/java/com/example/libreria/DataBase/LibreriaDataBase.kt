package com.example.libreria.DataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.libreria.Entities.LibroEntity
import com.example.libreria.Entities.UsuarioEntity
import com.example.libreria.Entities.UsuarioSigueLibroEntity
import com.example.libreria.Interfaces.LibroDao
import com.example.libreria.Interfaces.UsuarioDao
import com.example.libreria.Interfaces.UsuarioSigueLibro


@Database(entities = arrayOf( UsuarioEntity::class, LibroEntity::class, UsuarioSigueLibroEntity::class), version = 3)
abstract class DataBaseLibreria : RoomDatabase(){
        abstract fun libroDao(): LibroDao
        abstract fun usuarioDao(): UsuarioDao
        abstract fun usuariolibroDao(): UsuarioSigueLibro

}