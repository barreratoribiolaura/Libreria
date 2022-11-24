package com.example.libreria.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.libreria.Activities.HomeActivity
import com.example.libreria.Clases.Comprobaciones
import com.example.libreria.Clases.ImagenClass
import com.example.libreria.Entities.LibroEntity
import com.example.libreria.R
import com.example.libreria.databinding.ItemLibroBinding


class InicioAdapter(private var libros:MutableList<LibroEntity>,val usuario_id:Long?, private val
listener: HomeActivity
):RecyclerView.Adapter<InicioAdapter.ViewHolder>(){

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

            binding.titulo.text = libro.titulo
            val iv = ImagenClass()
            iv.loadImage(libro.portada,binding.portada,context)

        }
    }

    fun update(libroEntity: LibroEntity) {

        val index = libros.indexOf(libroEntity)
        if (index != -1) {
            libros.set(index, libroEntity)
            notifyItemChanged(index)
        }

    }

    fun setLibro(libros: MutableList<LibroEntity>) {
        this.libros = libros
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int = libros.size

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val binding = ItemLibroBinding.bind(view)
        fun setListener(libro: LibroEntity) {
            binding.root.setOnClickListener {
                    listener.onClick(libro)
                 }
            binding.root.setOnLongClickListener{
                listener.editarLibro(libro)
                true
            }
            binding.btnFavorito.setOnClickListener {
                    listener.onFavoriteLibro(libro)
            }
        }
    }
}


