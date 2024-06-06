package com.example.mynotes.model

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

    fun removeNota(position: Int) {
        notas.removeAt(position)
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

}
