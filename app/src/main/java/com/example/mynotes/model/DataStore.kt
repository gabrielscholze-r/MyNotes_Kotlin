package com.example.mynotes.model

object DataStore {
    val notas = mutableListOf<Nota>()

    fun addNota(nota: Nota) {
        notas.add(nota)
    }

    fun editNota(position: Int, nota: Nota) {
        notas[position] = nota
    }

    fun removeNota(position: Int) {
        notas.removeAt(position)
    }

    fun getNota(position: Int): Nota {
        return notas[position]
    }
}
