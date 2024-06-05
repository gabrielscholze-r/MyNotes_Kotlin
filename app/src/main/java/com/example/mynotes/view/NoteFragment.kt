package com.example.mynotes.view

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mynotes.controller.EditCreateNote
import com.example.mynotes.databinding.FragmentNotesBinding
import com.example.mynotes.model.DataStore
import com.example.mynotes.model.Nota
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

class NoteFragment : Fragment() {

    private lateinit var binding: FragmentNotesBinding
    private lateinit var adapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val notas = loadNotasFromInternalStorage()
        DataStore.notas.clear()
        DataStore.notas.addAll(notas)
        setupRecyclerView()
        setupAddButton()
    }

    private fun setupRecyclerView() {
        val notes = DataStore.notas
        val categorias = DataStore.categorias
        adapter = NoteAdapter(notes,
            { position -> editNote(position) },
            { position -> showDeleteDialog(position) }
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }
    private fun showDeleteDialog(position: Int) {
        val note = DataStore.notas[position]

        AlertDialog.Builder(requireContext()).run {
            setMessage("Tem certeza que deseja excluir esta nota?")
            setPositiveButton("Excluir") { _, _ ->
                DataStore.removeNota(position)
                adapter.notifyItemRemoved(position)
                Toast.makeText(requireContext(), "Nota '${note.titulo}' excluída com sucesso!!!", Toast.LENGTH_LONG).show()
            }
            setNegativeButton("Cancelar") { dialog, _ ->
                adapter.notifyItemChanged(position)
                dialog.dismiss()
            }
            show()
        }
    }
    private fun setupAddButton() {
        binding.btnAddNote.setOnClickListener {
            val intent = Intent(requireContext(), EditCreateNote::class.java)
            startActivityForResult(intent, ADD_NOTE_REQUEST_CODE)
        }
    }

    private fun editNote(position: Int) {
        val intent = Intent(requireContext(), EditCreateNote::class.java)
        intent.putExtra("position", position)
        startActivityForResult(intent, EDIT_NOTE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_NOTE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.let { intent ->
                adapter.notifyItemInserted(DataStore.notas.size - 1)
            }
        } else if (requestCode == EDIT_NOTE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.let { intent ->
                val position = intent.getIntExtra("position", -1)
                if (position != -1) {
                    adapter.notifyItemChanged(position)
                }
            }
        }
    }
    private fun loadNotasFromInternalStorage(): List<Nota> {
        val file = File(requireContext().filesDir, "notas.json")
        if (!file.exists()) {
            return emptyList()
        }

        val reader = file.reader()
        val notasType = object : TypeToken<List<Nota>>() {}.type
        return Gson().fromJson(reader, notasType)
    }


    companion object {
        private const val ADD_NOTE_REQUEST_CODE = 1
        private const val EDIT_NOTE_REQUEST_CODE = 2
    }
}