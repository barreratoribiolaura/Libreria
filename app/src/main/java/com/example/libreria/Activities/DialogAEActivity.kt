package com.example.libreria.Activities

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.libreria.Adapters.InicioAdapter
import com.example.libreria.Adapters.LibreriaAdapter
import com.example.libreria.DataBase.LibreriaApplication
import com.example.libreria.Entities.LibroEntity
import com.example.libreria.R
import com.example.libreria.databinding.ActivityDialogEaBinding
import com.example.libreria.databinding.ActivityHomeBinding
import com.google.android.material.textfield.TextInputLayout
import org.jetbrains.anko.doAsync


class DialogAEActivity : AppCompatActivity()  {

    private lateinit var binding: ActivityDialogEaBinding

    private lateinit var binding2: ActivityHomeBinding

    var libro = LibroEntity(titulo="", portada = "", resumen = "", npapginas = "", autor = "", creado = "", enlace = "")

    var color = ""

    private var librosAdapter = InicioAdapter(mutableListOf(),0,HomeActivity())
    private var librosAdapter2 = LibreriaAdapter(mutableListOf(),0,LibreriaActivity())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        binding = ActivityDialogEaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding2 = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val librosp= getSharedPreferences("libro", MODE_PRIVATE)
        val funcion = librosp.getString("funcion","")
        val titulo = librosp.getString("titulo","")

        //RECOJO EL LIBRO QUE HE PULSADO PARA LOS MÉTODOS DE EDITAR
        Thread{
            libro = LibreriaApplication.database.libroDao().buscarLibro(titulo)
        }.start()
        Thread.sleep(100)

        if(funcion.equals("crear")){
            setTitle("Crea tu libro");
            binding.btnConfirmar.setOnClickListener(){
                crearLibro()
            }
        }else{
            setTitle("Edita a '"+libro.titulo.toUpperCase()+"'");
            binding.textColor.visibility=GONE
            binding.eleccionColor.visibility=GONE

            binding.btnConfirmar.setOnClickListener(){
                editarLibro()
            }

        }

    }

    private fun editarLibro() {

        val titulo = binding.inputTitulo.text.toString().trim()
        val resumen = binding.inputResumen.text.toString().trim()
        val npaginas = binding.inputnPaginas.text.toString().trim()

        val titulo_antiguo = libro.titulo

        if(titulo.isNotEmpty()){
            libro.titulo = titulo
        }
        if(resumen.isNotEmpty()){
            libro.resumen = resumen
        }
        if (npaginas.isNotEmpty()){
            libro.npapginas = npaginas
        }

        //COMPRUEBA SI EL TÍTULO PUESTO YA EXISTE
        if(comprobarLibroBD(titulo)) { Toast.makeText(this,"Ese título ya existe", Toast.LENGTH_SHORT).show()
        }else{
            doAsync {
                LibreriaApplication.database.libroDao().updateLibro(libro)
            }

            Thread.sleep(200)

            librosAdapter.update(libro)
            librosAdapter2.update(libro)

            finish()

            Toast.makeText(this,"Libro '"+titulo_antiguo.toUpperCase()+"' editado", Toast.LENGTH_SHORT).show()
        }


    }

    private fun crearLibro() {
        val t = binding.tilTitulo
        val r = binding.tilresumen
        val np = binding.tilnPaginas
        var autor = ""
        var valido = true

        //COMPRUEBA SI ESTÁN LOS CAMPOS RELLENADOS
        val lista: List<TextInputLayout> = listOf(t,r,np)
        for (item in lista){
            if(item.editText?.text!!.isEmpty()){
                item.helperText= "REQUERIDO"
                item.isHelperTextEnabled=true
                valido = false
            }else{
                item.isHelperTextEnabled=false
            }
        }

        val userDetails = getSharedPreferences("userdetails", MODE_PRIVATE)
        val usuario = userDetails.getString("usuario","")
        val contrasenya = userDetails.getString("contrasenya","")

        //RECOJO EL USUARIO PARA PONERLO COMO AUTOR
        doAsync {
            val u = LibreriaApplication.database.usuarioDao().getUsuario(usuario, contrasenya)
            if(u.nombre.isNotEmpty()){
                autor = u.nombre
            }
        }
        Thread.sleep(70)

        if(valido){

            if(!comprobarLibroBD(t.editText?.text.toString().trim())){
                if(color!=""){

                    val l = LibroEntity(
                        titulo = t.editText?.text.toString().trim(),
                        portada = color,
                        enlace = "",
                        autor = autor,
                        resumen = r.editText?.text.toString().trim(),
                        npapginas = np.editText?.text.toString().trim(),
                        creado = "si"

                    )
                    Thread{
                        LibreriaApplication.database.libroDao().addLibro(l)
                    }.start()

                    Toast.makeText(this,"Libro' "+l.titulo.toUpperCase()+"' creado", Toast.LENGTH_SHORT).show()

                    finish()

                }else{
                    Toast.makeText(this,"No le has puesto portada", Toast.LENGTH_SHORT).show()
                }

            }else{
                Toast.makeText(this,"Ese título ya existe", Toast.LENGTH_SHORT).show()
            }
        }
    }
    //COMPRUEBA SI EL LIBRO EXISTE YA O NO
    private fun comprobarLibroBD(titulo:String?):Boolean{
        var existe = false
        doAsync {
            val l = LibreriaApplication.database.libroDao().buscarLibro(titulo)
            if(l.titulo.isNotEmpty()){
                existe = true

            }
        }

        Thread.sleep(70)

        return existe
    }

    //MÉTODO PARA AÑADIR UNA PORTADA A LOS LIBROS
    fun onRadioButtonClicked(view: View)  {

        if (view is RadioButton) {

            val checked = view.isChecked

            when (view.getId()) {
                R.id.btnYellow ->
                    if (checked) {
                        color = "https://thumbs.dreamstime.com/b/libro-amarillo-en-la-superficie-blanca-20485427.jpg"
                    }
                R.id.btnBlue ->
                    if (checked) {
                        color = "https://www.rastreator.mx/wp-content/uploads/libro-azul.png"
                    }
                R.id.btnGreen ->
                    if (checked) {
                        color = "https://img.freepik.com/fotos-premium/libro-verde-sobre-fondo-blanco_501761-515.jpg"
                    }
                R.id.btnRed ->
                    if (checked) {
                        color = "data:image/jpeg;base64,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"
                    }
            }
        }
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onPause() {
        super.onPause()
    }
}