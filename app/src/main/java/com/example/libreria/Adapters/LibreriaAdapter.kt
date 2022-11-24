package com.example.libreria.Adapters

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.libreria.Activities.LibreriaActivity
import com.example.libreria.Clases.Comprobaciones
import com.example.libreria.Clases.ImagenClass
import com.example.libreria.DataBase.LibreriaApplication
import com.example.libreria.Entities.LibroEntity
import com.example.libreria.R
import com.example.libreria.databinding.ItemLibroBinding
import org.jetbrains.anko.doAsync


class LibreriaAdapter(private var libros:MutableList<LibroEntity>,val usuario_id:Long, private val
listener: LibreriaActivity
):RecyclerView.Adapter<LibreriaAdapter.ViewHolder>(){

    private lateinit var context: Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        val view = LayoutInflater.from(context).inflate(R.layout.item_libro,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val libro = libros.get(position)
        //compruebo si es favorito o no para asignarle un corazón
        val favorito = Comprobaciones().comprobarLibroenFavoritos(usuario_id,libro.libro_id)
        with(holder){
            setListener(libro)
            if(favorito){
                binding.btnFavorito.setImageResource(R.drawable.ic_favorite)
            }else{
                binding.btnFavorito.setImageResource(R.drawable.ic_favoritefalse)
            }
            //si el libro está creado por un usuario se puede borrar
            if(libro.creado.equals("si")){
                binding.btnBorrar.visibility= View.VISIBLE
            }
            binding.titulo.text = libro.titulo

            val iv = ImagenClass()
            iv.loadImage(libro.portada,binding.portada,context)
        }
    }

    fun setLibro(libros: MutableList<LibroEntity>) {
        this.libros = libros
        notifyDataSetChanged()
    }

    fun deleteLibro(libroEntity: LibroEntity){
        //diálogo de alerta antes de borrar un libro
        val dialogClickListener =
            DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {

                    }
                    DialogInterface.BUTTON_NEGATIVE -> {
                        doAsync{
                            LibreriaApplication.database.libroDao().deleteLibro(libroEntity)
                        }
                        val index = libros.indexOf(libroEntity)
                        if (index != -1){
                            libros.removeAt(index)
                            notifyItemRemoved(index)
                        }
                    }
                }
            }

        val builder = AlertDialog.Builder(context)
        builder.setMessage("¿Estás seguro?").setPositiveButton("NO", dialogClickListener)
            .setNegativeButton("SI", dialogClickListener).show()

    }

    fun update(libroEntity: LibroEntity) {

        val index = libros.indexOf(libroEntity)
        if (index != -1) {
            libros.set(index, libroEntity)
            notifyItemChanged(index)
        }
    }

    override fun getItemCount(): Int = libros.size

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val binding = ItemLibroBinding.bind(view)
        fun setListener(libro: LibroEntity) {
            binding.root.setOnClickListener {
                listener.onClick(libro) }
            binding.root.setOnLongClickListener{
                listener.editarLibro(libro)
                true
            }
            binding.btnBorrar.setOnClickListener(){

                deleteLibro(libro)
            }

            binding.btnFavorito.setOnClickListener {
                listener.onFavoriteLibro(libro)
            }
        }
    }
}

