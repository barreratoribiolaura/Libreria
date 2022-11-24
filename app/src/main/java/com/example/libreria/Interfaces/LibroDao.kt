package com.example.libreria.Interfaces

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

import com.example.libreria.Entities.LibroEntity


@Dao
interface LibroDao {

    @Query("SELECT * FROM LibroEntity; ")
    fun getLibreria(): MutableList<LibroEntity>

    @Query("INSERT INTO LibroEntity(titulo,portada,autor,resumen,enlace,creado,npapginas) " +
            "values" +
            "('El Quijote','https://m.media-amazon.com/images/I/61xPqIiIjcL.jpg','Miguel de Cervantes','Hace ya 400 años que por la mente de muchas personas cabalga valiente un caballero andante, el de La Triste Figura le llaman, viviendo aventuras sin fin y luchando contra enemigos que no existen.','https://www.elejandria.com/libro/don-quijote-de-la-mancha/cervantes-miguel/77','no','462')," +
            "('Lazarillo de Tormes','https://www.clipsmerida.com//imagenes_grandes/9788467/978846704854.JPG','Anónimo','La novela Lazarillo de Tormes gira entorno a Lázaro, un niño ingenuo que, debido a las adversidades y complejidades que atraviesa, se convierte en un joven pícaro que lucha por sobrevivir. ','https://www.unaula.edu.co/sites/default/files/ebooks/LazarilloDeTormes.pdf','no','128')," +
            "('Ruination','https://m.media-amazon.com/images/I/61u8QCgPQoL.jpg','Anthony Reynolds','Camavor es una tierra desoladora cuyos cimientos se erigen sobre un legado de sangre y destrucción. Donde van los caballeros del imperio, la carnicería sigue. Kalista sueña con dar un vuelco a la situación y cambiar todo lo malo de su tierra.','https://www.casadellibro.com/libro-ruination-una-novela-de-league-of-legends/9788419421005/13082345','no','423')," +
            "('Metamorfosis','https://pictures.abebooks.com/inventory/md/md30873634388.jpg','Ovidio','Doscientas cincuenta historias, mitos y leyendas que abarcan desde el nacimiento de la humanidad y la creación del mundo, en la era de los cataclismos, hasta la muerte y apoteosis de César.','https://www.amazon.es/Metamorfosis-clasicos-Adaptados-Cl%C3%A1sicos-9788431694111/dp/8431694114/ref=asc_df_8431694114/?tag=googshopes-21&linkCode=df0&hvadid=195202597655&hvpos=&hvnetw=g&hvrand=7264831020175952347&hvpone=&hvptwo=&hvqmt=&hvdev=c&hvdvcmdl=&hvlocint=&hvlocphy=1005545&hvtargid=pla-125952502875&psc=1','no','245')," +
            "('Kotlin','https://www.alfaomega.com.mx/media/catalog/product/cache/1/image/728x/9df78eab33525d08d6e5fb8d27136e95/1/_/1_28_80.jpg','Luján Castillo','Kotlin es posiblemente la mejor noticia desde la aparición de Swift, el lenguaje de Apple, porque ofrece una sintaxis expresiva, un sistema de tipo intuitivo fuerte y un gran soporte de herramientas.','https://www.alfaomega.com.mx/default/aprende-a-programar-con-kotlin-8442.html','no','180')," +
            "('El Principito','https://static.fnac-static.com/multimedia/Images/ES/NR/03/9e/71/7446019/1520-1.jpg','Antoine de Saint-Exupéry','Un pequeño príncipe que parte de su asteroide a una travesía por el universo, en la cual descubre la extraña forma en que los adultos ven la vida y comprende el valor del amor y la amistad.','https://www.tiposinfames.com/libros/el-principito/72711/','no','115')," +
            "('Berserk I','https://cdn.shopify.com/s/files/1/0589/3308/4342/products/31mPTo5ndvL_grande.jpg?v=1645660865','Kentarō Miura','Un antihéroe oscuro y mal encarado, con un cuerpo hercúleo cincelado durante su desempeño como mercenario, sin un ápice de empatía por sus enemigos ni por sus semejantes. ','https://www.zonanegativa.com/berserk-la-leyenda-del-espadachin-negro/','no','456')," +
            "('The Witcher','https://b2b.asmodee.es/product/image/large/holwit01_1.jpg','Andrzej Sapkowski','Geralt de Rivia es el brujo, el Lobo Blanco, el célebre mutante mata monstruos del Continente. Temido y aborrecido por igual, el brujo recorre un mundo brutalmente hostil tratando de ganarse la vida.','https://www.elquintolibro.es/2020/01/resena-de-the-witcher-el-ultimo-deseo/','no','263')," +
            "('El Hobbit','https://static.fnac-static.com/multimedia/Images/ES/NR/33/2b/10/1059635/1540-6/tsp20160818214940/El-Hobbit-La-desolacion-de-Smaug-Ed-extendida-DVD.jpg','J.R.R Tolkien','Un gran clásico moderno y el preludio de las vastas y poderosas mitologías de El Señor de los Anillos.','https://www.casadellibro.com/libro-el-hobbit/9788445000656/2010570','no','288');")

    fun insertLibrosPred()

    @Query("DELETE FROM LibroEntity;")
    fun deleteLibrosPred()

    @Query("SELECT * FROM LibroEntity where autor==:usuario;")
    fun getMisLibros(usuario:String?) : MutableList<LibroEntity>

    @Query("SELECT * FROM LibroEntity where LibroEntity.titulo = :titulo;")
    fun buscarLibro(titulo:String?) :LibroEntity

    @Insert
    fun addLibro(libroEntity: LibroEntity)

    @Update
    fun updateLibro(libroEntity: LibroEntity)

    @Delete
    fun deleteLibro(libroEntity: LibroEntity)




}