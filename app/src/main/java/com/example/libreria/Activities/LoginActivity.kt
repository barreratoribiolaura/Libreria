package com.example.libreria.Activities

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.libreria.Clases.Comprobaciones
import com.example.libreria.DataBase.LibreriaApplication
import com.example.libreria.Entities.UsuarioEntity
import com.example.libreria.databinding.ActivityLoginBinding
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //estos dos métodos son para meter los libros predeterminados o eliminarlos

        //deleteLibrosPred()

        //añadirPredeterminado()

        /*-----------------------------------------------------------------------*/

        comprobarCheck(getRecordarSP())

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener(){
            login()
        }

        binding.btnRegistrarse.setOnClickListener(){
            registrarUsuario()
        }

    }

    //estos dos métodos son para meter los libros predeterminados o eliminarlos

    private fun deleteLibrosPred() {
        Thread{
            LibreriaApplication.database.libroDao().deleteLibrosPred()
            LibreriaApplication.database.usuarioDao().deleteUsuarios()
            LibreriaApplication.database.usuariolibroDao().deleteRelacionesPred()

        }.start()
    }

    private fun añadirPredeterminado() {
        Thread{
            LibreriaApplication.database.libroDao().insertLibrosPred()
            LibreriaApplication.database.usuarioDao().insertUsuarioPred()
            LibreriaApplication.database.usuariolibroDao().insertUsuarioSiguePred()
        }.start()
    }

    /*--------------------------------------------------------------------------------------------*/

    private fun comprobarCheck(r:SharedPreferences){
        val recordar = r.getString("recordar","")
        if(recordar=="true"){
            entrar() }
    }

    private fun login(){

        val nombretil = binding.tilUsuario
        val contrasenyatil = binding.tilContrasenya

        if (comprobarTextInputs(nombretil,contrasenyatil)){
            if(comprobarUsuarioBD()){
                if(binding.checkbox.isChecked){
                    recordarUsuario()
                }else{
                    olvidarUsuario()
                }
                pasarUsuarioporSP(nombretil.editText?.text.toString().trim(),
                    contrasenyatil.editText?.text.toString().trim())

                entrar()
            }else{
                Toast.makeText(this,"El usuario no existe", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registrarUsuario(){

        val nombretil = binding.tilUsuario
        val contrasenyatil = binding.tilContrasenya

        if (comprobarTextInputs(nombretil,contrasenyatil)){
            if(!usuarioRegistrado()){
                val usuario = UsuarioEntity(
                    nombre = nombretil.editText?.text.toString(),
                    contrasenya = contrasenyatil.editText?.text.toString()
                )

                Thread {
                    LibreriaApplication.database.usuarioDao().addUsuario(usuario)
                }.start()

                Toast.makeText(this,"Registrado correctamente", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this,"Ese nombre de usuario ya está registrado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun comprobarTextInputs(nombretxt: TextInputLayout, contrasenyatxt: TextInputLayout) : Boolean{

        var valido = true

        val lista:List<TextInputLayout> = listOf(nombretxt,contrasenyatxt)
        for (inputLayout in lista){
            if (inputLayout.editText?.text!!.isEmpty()){
                inputLayout.helperText= "REQUERIDO"
                inputLayout.isHelperTextEnabled=true
                valido = false
            }else{
                inputLayout.isHelperTextEnabled=false
            }
        }
        return valido
    }

    private fun comprobarUsuarioBD() : Boolean{

        val nombre = binding.inputUsuario.text.toString()
        val contrasenya = binding.inputContrasenya.text.toString()

        return Comprobaciones().comprobarUsuarioBD(nombre, contrasenya)
    }

    private fun usuarioRegistrado() : Boolean{
        val nombre = binding.inputUsuario.text.toString()
        return Comprobaciones().usuarioRegistrado(nombre)

    }

    private fun recordarUsuario(){
        val edit: SharedPreferences.Editor = getRecordarSP().edit()
        edit.putString("recordar","true")
        edit.apply()
    }


    private fun olvidarUsuario(){
        val edit: SharedPreferences.Editor = getUserDetails().edit()
        edit.clear()
        edit.apply()
    }

    private fun pasarUsuarioporSP(nombre: String, contrasenya: String) {
        val edit: SharedPreferences.Editor = getUserDetails().edit()
        edit.putString("usuario", nombre)
        edit.putString("contrasenya", contrasenya)
        edit.apply()
    }
    private fun entrar(){
        finish()
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    private fun getUserDetails() : SharedPreferences{
        val userDetails = getSharedPreferences("userdetails", MODE_PRIVATE)
        return userDetails
    }

    private fun getRecordarSP() : SharedPreferences{
        val recordar = getSharedPreferences("recordar", MODE_PRIVATE)
        return recordar
    }
}