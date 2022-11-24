package com.example.libreria.Clases

import android.content.SharedPreferences
import com.example.libreria.DataBase.LibreriaApplication
import com.example.libreria.Entities.LibroEntity
import com.example.libreria.Entities.UsuarioEntity
import org.jetbrains.anko.doAsync

class Comprobaciones() {

    fun buscarLibro(sp:SharedPreferences) : LibroEntity{

        val titulo = sp.getString("titulo","")

        var libro = LibroEntity(titulo="", portada = "", resumen = "", npapginas = "", autor = "", creado = "", enlace = "")

        Thread{
            libro = LibreriaApplication.database.libroDao().buscarLibro(titulo)
        }.start()
        Thread.sleep(100)

        return libro
    }

    fun getUsuario(userDetails:SharedPreferences) : UsuarioEntity{

        val u = userDetails.getString("usuario","")
        val c = userDetails.getString("contrasenya","")

        var usuario = UsuarioEntity(0,"","")

        doAsync{
            val user = LibreriaApplication.database.usuarioDao().getUsuario(u,c)
            usuario = user
        }

        Thread.sleep(100)

        return usuario
    }

    fun comprobarUsuarioBD(nombre: String, contrasenya: String) : Boolean {

        var existe = false

        doAsync {
            val u = LibreriaApplication.database.usuarioDao().getUsuario(nombre, contrasenya)
            if(u.nombre.isNotEmpty()){
                existe = true
            }
        }

        Thread.sleep(70)

        return existe
    }

    fun usuarioRegistrado(nombre : String) : Boolean {

        var existe = false

        doAsync {
            val u = LibreriaApplication.database.usuarioDao().comprobarUsuarioLogin(nombre)
            if(u.nombre.isNotEmpty()){
                existe = true
            }
        }

        Thread.sleep(70)

        return existe
    }

    fun comprobarLibroenFavoritos(usuario_id: Long?, libro_id: Long):Boolean{
        var esFavorito = false

        doAsync {
            esFavorito = LibreriaApplication.database.usuariolibroDao().comprobarFavorito(usuario_id,libro_id)
        }
        Thread.sleep(50)

        return esFavorito
    }


}