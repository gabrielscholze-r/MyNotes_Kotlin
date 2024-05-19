package com.example.mynotes.controller

import android.app.Activity
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

        position = intent.getIntExtra("position", -1)
        if (position != -1) {
            setData(position)
        }

        binding.btnSave.setOnClickListener {
            saveNota()
        }

        binding.btnCancel.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

    private fun saveNota() {
        val titulo = binding.txtTitulo.text.toString()
        val conteudo = binding.txtConteudo.text.toString()

        if (titulo.length > 50 || conteudo.length > 200) {
            showMessage("Limite de caracteres excedido! Título: 50, Conteúdo: 200")
            return
        }

        if (position == -1) {
            DataStore.addNota(Nota(titulo, conteudo))
        } else {
            DataStore.editNota(position, Nota(titulo, conteudo))
        }
        setResult(Activity.RESULT_OK)
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