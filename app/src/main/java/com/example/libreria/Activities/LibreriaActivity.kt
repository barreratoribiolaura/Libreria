package com.example.libreria.Activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.libreria.Adapters.LibreriaAdapter
import com.example.libreria.Clases.Comprobaciones
import com.example.libreria.DataBase.LibreriaApplication
import com.example.libreria.Entities.LibroEntity
import com.example.libreria.Entities.UsuarioEntity
import com.example.libreria.Interfaces.OnClickListener
import com.example.libreria.R
import com.example.libreria.databinding.ActivityLibreriaBinding
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class LibreriaActivity : AppCompatActivity(), OnClickListener {

    private lateinit var binding: ActivityLibreriaBinding

    private lateinit var librosAdapter: LibreriaAdapter
    private lateinit var layout: GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_libreria)

        binding = ActivityLibreriaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        comprobarModoApp()

        setupRecyclerView()

    }

    private fun comprobarModoApp() {
        val modoapp = getSharedPreferences("modoapp", MODE_PRIVATE)

        if(modoapp.getString("cambiarlo","").equals("si")){
            cambiarModoApp(modoapp)
        }
    }

    private fun cambiarModoApp(modoapp : SharedPreferences){
        if(modoapp.getString("modo","").equals("oscuro")){
            binding.fondoLibreria.setBackgroundColor(resources.getColor(R.color.colorOscuro))
            binding.txtLibreria.setTextColor(resources.getColor(R.color.white))
        }else{
            binding.fondoLibreria.setBackgroundColor(resources.getColor(R.color.white))
            binding.txtLibreria.setTextColor(resources.getColor(R.color.black))
        }
    }

    private fun getLibros(){
        doAsync {
            val libros = LibreriaApplication.database.libroDao().getLibreria()
            uiThread {
                librosAdapter.setLibro(libros)
            }
        }
    }

    private fun setupRecyclerView() {

        librosAdapter = LibreriaAdapter(mutableListOf(), Comprobaciones().getUsuario(getUserDetails()).usuario_id,this)
        layout = GridLayoutManager(this,2)
        getLibros()
        binding.recyclerViewLibreria.apply {
            setHasFixedSize(true)
            layoutManager = layout
            adapter = librosAdapter
        }
    }

    override fun onClick(libroEntity: LibroEntity) {

        val titulo = getSharedPreferences("titulo", MODE_PRIVATE)
        val edit: SharedPreferences.Editor = titulo.edit()
        edit.clear()
        edit.putString("titulo", libroEntity.titulo)
        edit.apply()
        val intent = Intent(this,ItemLibroActivity()::class.java)
        startActivity(intent);
    }

    override fun onDeleteLibro(libroEntity: LibroEntity) {

        val usuario = Comprobaciones().getUsuario(getUserDetails())
        doAsync{
            LibreriaApplication.database.libroDao().deleteLibro(libroEntity)
            LibreriaApplication.database.usuariolibroDao().deleteRelacion(usuario.usuario_id,
                libroEntity.libro_id)
            uiThread {
                librosAdapter.deleteLibro(libroEntity)
            }
        }
    }

    override fun onFavoriteLibro(libroEntity: LibroEntity) {

        val usuarioEntity = Comprobaciones().getUsuario(getUserDetails())

        val existe = Comprobaciones().comprobarLibroenFavoritos(usuarioEntity.usuario_id,libroEntity.libro_id)

        if(existe){
            seguirLibro(usuarioEntity,libroEntity)
            Toast.makeText(this,"Ya no sigues a '"+libroEntity.titulo.toUpperCase()+"'",
                Toast.LENGTH_SHORT).show()

        }else{
            dejardeSeguirLibro(usuarioEntity,libroEntity)
            Toast.makeText(this,"Has seguido a '"+libroEntity.titulo.toUpperCase()+"'",
                Toast.LENGTH_SHORT).show()
        }
    }

    fun editarLibro(libroEntity: LibroEntity) {

        if(libroEntity.creado.equals("si")){
            val sp = getSharedPreferences("libro", MODE_PRIVATE)
            val edit: SharedPreferences.Editor = sp.edit()
            edit.putString("funcion", "editar")
            edit.putString("titulo", libroEntity.titulo.toString().trim())
            edit.apply()
            startActivity(Intent(this,DialogAEActivity::class.java))
        }else{
            Toast.makeText(this,"No puede ser modificado", Toast.LENGTH_SHORT).show()
        }
    }

    fun getUserDetails() : SharedPreferences{
        val userDetails = getSharedPreferences("userdetails", MODE_PRIVATE)
        return userDetails
    }

    fun seguirLibro(usuarioEntity: UsuarioEntity, libroEntity: LibroEntity){
        Thread{
            LibreriaApplication.database.usuariolibroDao().dejardeSeguir(libroEntity.libro_id,
                usuarioEntity.usuario_id)
        }.start()
        Thread.sleep(50)
        librosAdapter.update(libroEntity)
    }

    fun dejardeSeguirLibro(usuarioEntity: UsuarioEntity, libroEntity: LibroEntity){
        Thread{
            LibreriaApplication.database.usuariolibroDao().seguirLibro(libroEntity.libro_id,
                usuarioEntity.usuario_id)
        }.start()
        Thread.sleep(50)
        librosAdapter.update(libroEntity)
    }

    override fun onResume() {
        super.onResume()
        cargarRecyclerView()
    }

    private fun cargarRecyclerView() {
        setupRecyclerView()
    }
}