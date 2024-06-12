package com.example.mynotes.model

data class Nota(
    var id:Int,
    var titulo: String,
    var conteudo: String,
    var categoria: Categoria
)