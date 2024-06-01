package com.example.mynotes.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mynotes.databinding.AdapterCategoriaBinding
import com.example.mynotes.model.Categoria

class CategoriasAdapter(
    private val categorias: MutableList<Categoria>
) : RecyclerView.Adapter<CategoriasAdapter.CategoriasHolder>(){

    inner class CategoriasHolder(private val binding: AdapterCategoriaBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(categoria: Categoria, position:Int){
            binding.txtCategoria.text=categoria.nome
            print(categoria.nome)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriasHolder {
        val binding = AdapterCategoriaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoriasHolder(binding)

    }



    override fun onBindViewHolder(holder: CategoriasHolder, position: Int) {
        holder.bind(categorias[position],position)
    }
    override fun getItemCount(): Int =categorias.size

}