package com.example.libreria.Activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.libreria.Adapters.InicioAdapter
import com.example.libreria.Clases.Comprobaciones
import com.example.libreria.DataBase.LibreriaApplication
import com.example.libreria.Entities.LibroEntity
import com.example.libreria.Entities.UsuarioEntity
import com.example.libreria.Interfaces.OnClickListener
import com.example.libreria.R
import com.example.libreria.databinding.ActivityHomeBinding
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class HomeActivity : AppCompatActivity() , OnClickListener {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var librosAdapter: InicioAdapter
    private lateinit var librosAdapter2: InicioAdapter
    private lateinit var layout: LinearLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ponerMsgBienvenida()

        cargarRecyclerViews()

        //COLOR DE LA APP
        val modoapp = getSharedPreferences("modoapp", MODE_PRIVATE)
        modoapp.edit().putString("modo","claro").apply()


        binding.btnLibreria.setOnClickListener(){
           abrirLibreria() }

        binding.crearLibro.setOnClickListener(){
            modoCrear()
            startActivity(Intent(this,DialogAEActivity::class.java)) }

        binding.btnCerrarSesion.setOnClickListener(){
            cerrarSesion() }


        binding.btnCambiarTono.setOnClickListener(){
            cambiarModoApp(modoapp)
            }


    }


    private fun cambiarModoApp(modoapp : SharedPreferences) {

        if (modoapp.getString("modo", "").equals("oscuro")) {
            modoClaro()
            modoapp.edit().putString("modo","claro").apply()
        } else {
            modoOscuro()
            modoapp.edit().putString("modo","oscuro").apply()
        }

        modoapp.edit().putString("cambiarlo","si").apply()
    }


    private fun cargarRecyclerViews() {
        setupRecyclerViewMiBiblioteca()
        setupRecyclerViewMisLibros()
    }

    private fun ponerMsgBienvenida() {

        val usuario = getUserDetails().getString("usuario","")
        binding.txtUsuario.setText(usuario.toString().toUpperCase())
    }

    private fun abrirLibreria() {

        val intent = Intent(this, LibreriaActivity::class.java)
        startActivity(intent)
    }

    //LIMPIO LOS SHARED PREFERENCES Y CIERRO LA ACTIVITY
    private fun cerrarSesion() {
        val recordar = getSharedPreferences("recordar", MODE_PRIVATE)
        val edit: SharedPreferences.Editor = recordar.edit()
        edit.clear()
        edit.apply()
        val modoapp = getSharedPreferences("modoapp", MODE_PRIVATE)
        val edit2: SharedPreferences.Editor = modoapp.edit()
        edit2.clear()
        edit2.apply()
        finish()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    //LIBROS SEGUIDOS
    private fun getLibrosMiBiblioteca(){

        val usuario = Comprobaciones().getUsuario(getUserDetails())

        binding.avisoMiBlibioteca.visibility=GONE

        doAsync {
            val libros = LibreriaApplication.database.usuariolibroDao()
                .getMiBiblioteca(usuario.usuario_id)

            uiThread {
                if(libros.isEmpty()){
                    binding.avisoMiBlibioteca.visibility=VISIBLE
                }
                librosAdapter.setLibro(libros)
            }
        }
        Thread.sleep(100)
    }

    //LIBROS CREADOS POR EL USUARIO
    private fun getMisLibros(){

        val usuario = getUserDetails().getString("usuario","")

        binding.avisoMisLibros.visibility=GONE
        doAsync {
            val libros = LibreriaApplication.database.libroDao().getMisLibros(usuario)

            uiThread {

                if(libros.isEmpty()){
                    binding.avisoMisLibros.visibility=VISIBLE
                }
                librosAdapter2.setLibro(libros)
            }
        }
        Thread.sleep(100)
    }

    fun setupRecyclerViewMiBiblioteca() {

        //aquí le paso el id del usuario al Adapter para poder hacer comprobaciones para los libros seguidos
        librosAdapter = InicioAdapter(mutableListOf(),Comprobaciones().getUsuario(getUserDetails()).usuario_id, this)

        layout = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        getLibrosMiBiblioteca()
        binding.recyclerViewMiBiblioteca.apply {
            setHasFixedSize(true)
            layoutManager = layout
            adapter = librosAdapter
        }
    }

    fun setupRecyclerViewMisLibros() {

        //aquí le paso el id del usuario al Adapter para poder hacer comprobaciones para los libros seguidos
        librosAdapter2 = InicioAdapter(mutableListOf(),Comprobaciones().getUsuario(getUserDetails()).usuario_id, this,)

        layout = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        getMisLibros()

        binding.recyclerViewMisLibros.apply {
            setHasFixedSize(true)
            layoutManager = layout
            adapter = librosAdapter2
        }
    }

    //ABRE EL DIALOG DE LOS DETALLES DEL LIBRO
    override fun onClick(libroEntity: LibroEntity) {

        val titulo = getSharedPreferences("titulo", MODE_PRIVATE)
        val edit: SharedPreferences.Editor = titulo.edit()
        edit.clear()
        edit.putString("titulo", libroEntity.titulo)
        edit.apply()
        val intent = Intent(this,ItemLibroActivity()::class.java)
        startActivity(intent);
    }

    //MÉTODO PARA
    override fun onFavoriteLibro(libroEntity: LibroEntity) {

        val usuarioEntity = Comprobaciones().getUsuario(getUserDetails())

        val esFavorito = Comprobaciones().comprobarLibroenFavoritos(usuarioEntity.usuario_id,libroEntity.libro_id)

        if(esFavorito){
            seguirLibro(usuarioEntity,libroEntity)
            Toast.makeText(this,"Ya no sigues a '"+libroEntity.titulo.toUpperCase()+"'",
                Toast.LENGTH_SHORT).show()
        }else{
            dejardeSeguirLibro(usuarioEntity,libroEntity)
            Toast.makeText(this,"Has seguido a '"+libroEntity.titulo.toUpperCase()+"'",
                Toast.LENGTH_SHORT).show()

            binding.avisoMiBlibioteca.visibility=GONE
            }
        setupRecyclerViewMiBiblioteca()
    }


    fun editarLibro(libroEntity: LibroEntity) {

        if(libroEntity.creado.equals("si")){
            modoEditar(libroEntity)
            startActivity(Intent(this,DialogAEActivity::class.java))
        }else{
            Toast.makeText(this,"No puede editarse", Toast.LENGTH_SHORT).show()
        }
    }

    private fun modoCrear(){
        val funcion = getSharedPreferences("libro", MODE_PRIVATE)
        val edit: SharedPreferences.Editor = funcion.edit()
        edit.putString("funcion","crear")
        edit.apply()
    }

    private fun modoEditar(libroEntity: LibroEntity) {
        val sp = getSharedPreferences("libro", MODE_PRIVATE)
        val edit: SharedPreferences.Editor = sp.edit()
        edit.putString("funcion", "editar")
        edit.putString("titulo", libroEntity.titulo.toString().trim())
        edit.apply()
    }

    fun seguirLibro(usuarioEntity: UsuarioEntity,libroEntity: LibroEntity){
        Thread{
            LibreriaApplication.database.usuariolibroDao().dejardeSeguir(libroEntity.libro_id,
                usuarioEntity.usuario_id)
        }.start()
        Thread.sleep(50)
        librosAdapter.update(libroEntity)
        librosAdapter2.update(libroEntity)
    }

    fun dejardeSeguirLibro(usuarioEntity: UsuarioEntity,libroEntity: LibroEntity){
        Thread{
            LibreriaApplication.database.usuariolibroDao().seguirLibro(libroEntity.libro_id,
                usuarioEntity.usuario_id)
        }.start()
        Thread.sleep(50)
        librosAdapter.update(libroEntity)
        librosAdapter2.update(libroEntity)
    }

    fun getUserDetails() : SharedPreferences{
        val userDetails = getSharedPreferences("userdetails", MODE_PRIVATE)

        return userDetails
    }

    //están hechos a mano porque Javier pedía que fueran personalizados,
    //además de que las imágenes no son png, tienen fondo blanco
    //y quedaba muy feo la cardview negra con eso blanco, así que lo he personalizado yo entero

    fun modoClaro(){
        binding.inicioFondoRedondo.setBackgroundResource(R.drawable.inicio_fondo_redondo_claro)
        binding.textMiBiblioteca.setTextColor(resources.getColor(R.color.colorOscuro))
        binding.textMisLibros.setTextColor(resources.getColor(R.color.colorOscuro))
        binding.avisoMisLibros.setTextColor(resources.getColor(R.color.colorOscuro))
        binding.avisoMiBlibioteca.setTextColor(resources.getColor(R.color.colorOscuro))
        binding.toolBar.background.setTint(resources.getColor(R.color.white))
        binding.btnLibreria.setImageResource(R.drawable.ic_libreria)
        binding.btnCambiarTono.setImageResource(R.drawable.ic_modooscuro)
        binding.crearLibro.setBackgroundColor(resources.getColor(R.color.colorOscuro))
        binding.btnCerrarSesion.setImageResource(R.drawable.ic_cerrar_sesion)
    }
    fun modoOscuro() {
        binding.inicioFondoRedondo.setBackgroundResource(R.drawable.inicio_fondo_redondo_oscuro)
        binding.textMiBiblioteca.setTextColor(resources.getColor(R.color.white))
        binding.textMisLibros.setTextColor(resources.getColor(R.color.white))
        binding.avisoMisLibros.setTextColor(resources.getColor(R.color.white))
        binding.avisoMiBlibioteca.setTextColor(resources.getColor(R.color.white))
        binding.toolBar.background.setTint(resources.getColor(R.color.colorOscuro))
        binding.btnLibreria.setImageResource(R.drawable.ic_libreria_claro)
        binding.btnCambiarTono.setImageResource(R.drawable.ic_modoclaro)
        binding.btnCerrarSesion.setImageResource(R.drawable.ic_cerrar_sesion_claro)
    }
    override fun onResume() {
        super.onResume()
        cargarRecyclerViews()
    }
    override fun onDeleteLibro(libroEntity: LibroEntity) {
        TODO("No se usa en este activity pero viene de la interfaz")
    }
}















