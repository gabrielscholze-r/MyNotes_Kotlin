package com.example.mynotes.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import java.io.File

object DataStore {
    var notas = mutableListOf<Nota>()
    var categorias = mutableListOf<Categoria>()
    fun addNota(nota: Nota) {
        notas.add(nota)
    }

    fun editNota(position: Int, nota: Nota) {
        notas[position] = nota
    }
    fun getNota(position: Int): Nota{
        return notas[position]
    }

    fun removeNota(id: Int) {
        notas.removeAll{ it.id == id }
    }
    fun getAllCategorias(): List<Categoria>{
        return categorias
    }
    fun getCategoria(position: Int): Categoria {
        return categorias[position]
    }fun addCategoria(c: Categoria) {
        if(!categorias.contains(c)) {
            categorias.add(c)
        }
    }

    fun editCategoria(position: Int, categoria: Categoria) {
        categorias[position] = categoria
    }

    fun removeCategoria(position: Int) {
        categorias.removeAt(position)
    }
    fun getNoteByID(id: Int): Nota{
        return notas.filter {it.id==id}.get(0)
    }

}
