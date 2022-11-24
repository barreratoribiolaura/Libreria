package com.example.libreria.Clases

import android.content.Context
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class ImagenClass (){


    fun loadImage(url: String,iv: ImageView,act:Context) {
        Glide.with(act)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .fitCenter()
            .into(iv)
    }
}