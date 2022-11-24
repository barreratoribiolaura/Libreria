package com.example.libreria.Interfaces

import com.example.libreria.Entities.LibroEntity

interface OnClickListener {
    fun onClick(libroEntity: LibroEntity)
    fun onDeleteLibro(libroEntity: LibroEntity)
    fun onFavoriteLibro(libroEntity: LibroEntity)
}