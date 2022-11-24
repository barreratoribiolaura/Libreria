package com.example.libreria.Activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.libreria.Clases.Comprobaciones
import com.example.libreria.Clases.ImagenClass
import com.example.libreria.DataBase.LibreriaApplication
import com.example.libreria.Entities.LibroEntity
import com.example.libreria.databinding.ActivityItemLibroBinding

class ItemLibroActivity() : AppCompatActivity() {

    private lateinit var binding: ActivityItemLibroBinding

    val openURL = Intent(Intent.ACTION_VIEW)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTitle("")

        binding = ActivityItemLibroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sp = getSharedPreferences("titulo",MODE_PRIVATE)

        val libro = Comprobaciones().buscarLibro(sp)

        val iv = ImagenClass()
        iv.loadImage(libro.portada,binding.imgLibro,this)

        cargarInformacion(libro)

        binding.btnEnlace.setOnClickListener(){
        //COMPRUEBA SI EL LIBRO TIENE O NO ENLACE PARA REDIRIGIRLO
            if(libro.enlace.isNotEmpty()){
                openURL.data= Uri.parse(libro.enlace)
                startActivity(openURL)
            }else{
                Toast.makeText(this,"No tiene enlace", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun cargarInformacion(libro: LibroEntity) {
        binding.titulo.setText(libro.titulo)
        binding.npaginas.setText("Páginas: "+libro.npapginas)
        binding.autor.setText(libro.autor)
        binding.resumen.setText(libro.resumen)
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onPause() {
        super.onPause()
    }

}