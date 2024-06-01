package com.example.mynotes.model

object DataStore {
    var notas = mutableListOf<Nota>()
    var categorias = mutableListOf<Categoria>()

    init {
        categorias.add(Categoria("Trabalho"))
        categorias.add(Categoria("Estudos"))
        categorias.add(Categoria("Pessoal"))
        categorias.add(Categoria("Finanças"))
        categorias.add(Categoria("Saúde"))
        categorias.add(Categoria("Lazer"))
        categorias.add(Categoria("Projetos"))
        categorias.add(Categoria("Família"))
        categorias.add(Categoria("Viagens"))
        categorias.add(Categoria("Compras"))
    }
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

    fun getCategoria(position: Int): Categoria {
        return categorias[position]
    }fun addCategoria(c: Categoria) {
        categorias.add(c)
    }

    fun editCategoria(position: Int, categoria: Categoria) {
        categorias[position] = categoria
    }

    fun removeCategoria(position: Int) {
        categorias.removeAt(position)
    }

}
