package com.example.mynotes.controller

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mynotes.databinding.ActivityEditarNotaBinding
import com.example.mynotes.model.DataStore
import com.example.mynotes.model.Nota

class EditCreateNote : AppCompatActivity() {

    private lateinit var binding: ActivityEditarNotaBinding
    private var position: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarNotaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.getIntExtra("position", -1).apply {
            position = this
            if (position != -1)
                setData(position)
        }

        binding.btnSave.setOnClickListener {
            getData()?.let { nota ->
                saveNota(nota)
            } ?: run {
                showMessage("Campos inválidos!!!")
            }
        }

        binding.btnCancel.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

    private fun getData(): Nota? {
        val titulo = binding.txtTitulo.text.toString()
        val conteudo = binding.txtConteudo.text.toString()

        if (titulo.isEmpty() || conteudo.isEmpty())
            return null

        return Nota(titulo, conteudo)
    }

    private fun saveNota(nota: Nota) {
        if (position == -1) {
            // Adding a new note
            DataStore.addNota(nota)
        } else {
            // Editing an existing note
            DataStore.editNota(position, nota)
        }

        val resultIntent = Intent().apply {
            putExtra("position", position)
        }
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    private fun setData(position: Int) {
        val nota = DataStore.getNota(position)
        binding.txtTitulo.setText(nota.titulo)
        binding.txtConteudo.setText(nota.conteudo)
    }

    private fun showMessage(message: String) {
        AlertDialog.Builder(this).run {
            setTitle("Notas App")
            setMessage(message)
            setPositiveButton("Ok", null)
            show()
        }
    }
}
