package com.example.mynotes.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mynotes.databinding.AdapterNotesBinding
import com.example.mynotes.model.Nota

class NoteAdapter(
    private val notes: MutableList<Nota>,
    private val onNoteClick: (Int) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteHolder>() {

    inner class NoteHolder(private val binding: AdapterNotesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(nota: Nota, position: Int) {
            binding.txtTitulo.text = nota.titulo
            binding.txtConteudo.text = if (nota.conteudo.length > 15) {
                "${nota.conteudo.substring(0, 20)}..."
            } else {
                nota.conteudo
            }
            binding.root.setOnClickListener { onNoteClick(position) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val binding = AdapterNotesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        holder.bind(notes[position], position)
    }

    override fun getItemCount(): Int = notes.size
}
