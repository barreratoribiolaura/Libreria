package com.example.libreria.DataBase

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class LibreriaApplication: Application() {

    companion object {
        lateinit var database: DataBaseLibreria
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            this,
            DataBaseLibreria::class.java,
            "DataBaseLibreria")
            .build()
    }
}