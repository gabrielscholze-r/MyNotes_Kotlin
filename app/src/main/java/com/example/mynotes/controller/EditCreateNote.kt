package com.example.mynotes.controller

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.mynotes.databinding.ActivityEditarNotaBinding
import com.example.mynotes.model.Categoria
import com.example.mynotes.model.DataStore
import com.example.mynotes.model.Nota
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.InputStreamReader

class EditCreateNote : AppCompatActivity() {

    private lateinit var binding: ActivityEditarNotaBinding
    private var position: Int = -1
    private lateinit var categorias: List<Categoria>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarNotaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        copyJsonToInternalStorage()

        categorias = loadCategoriasFromInternalStorage()

        val categoriaNomes = categorias.map { it.nome }.toMutableList()
        DataStore.categorias = categorias.map { Categoria(it.nome) }.toMutableList()
        categoriaNomes.add("+ Nova Categoria")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoriaNomes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter

        intent.getIntExtra("position", -1).apply {
            position = this
            if (position != -1) {
                setData(position)
            }
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

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (categoriaNomes[position] == "+ Nova Categoria") {
                    showCriarCategoriaDialog()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun getData(): Nota? {
        val titulo = binding.txtTitulo.text.toString()
        val conteudo = binding.txtConteudo.text.toString()
        val categoria = binding.spinner.selectedItem.toString()
        if (titulo.isEmpty() || conteudo.isEmpty())
            return null

        return Nota(titulo, conteudo, Categoria(categoria))
    }

    private fun saveNota(nota: Nota) {
        if (position == -1) {
            DataStore.addNota(nota)
        } else {
            // Editar uma nota existente
            DataStore.editNota(position, nota)
        }

        val resultIntent = Intent().apply {
            putExtra("position", position)
        }
        saveNotas(DataStore.notas)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    private fun setData(position: Int) {
        val nota = DataStore.getNota(position)
        binding.txtTitulo.setText(nota.titulo)
        binding.txtConteudo.setText(nota.conteudo)
        val categoriaIndex = categorias.indexOfFirst { it.nome == nota.categoria.nome }
        binding.spinner.setSelection(categoriaIndex)
    }

    private fun showMessage(message: String) {
        AlertDialog.Builder(this).run {
            setTitle("Notas App")
            setMessage(message)
            setPositiveButton("Ok", null)
            show()
        }
    }

    private fun showCriarCategoriaDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Criar Nova Categoria")
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("Criar") { _, _ ->
            val novaCategoria = input.text.toString()
            if (novaCategoria.isNotEmpty()) {
                val novaCategoriaObj = Categoria(novaCategoria)
                DataStore.addCategoria(novaCategoriaObj)

                val categoriasMutable = categorias.toMutableList()
                categoriasMutable.add(categoriasMutable.size - 1, novaCategoriaObj)
                categorias = categoriasMutable.toList()
                val adapter = binding.spinner.adapter as ArrayAdapter<String>
                adapter.insert(novaCategoria, adapter.count - 1)
                binding.spinner.setSelection(adapter.count - 2)


                saveCategoriasToInternalStorage(categorias)
            } else {
                showMessage("Nome da categoria não pode estar vazio.")
            }
        }
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun copyJsonToInternalStorage() {
        val fileName = "categorias.json"
        val file = File(filesDir, fileName)
        if (!file.exists()) {
            assets.open(fileName).use { inputStream ->
                file.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        }
    }

    private fun loadCategoriasFromInternalStorage(): List<Categoria> {
        val file = File(filesDir, "categorias.json")
        val reader = file.reader()
        val categoriasType = object : TypeToken<List<Categoria>>() {}.type
        return Gson().fromJson(reader, categoriasType)
    }

    private fun saveCategoriasToInternalStorage(categorias: List<Categoria>) {
        val file = File(filesDir, "categorias.json")
        val writer = file.writer()
        Gson().toJson(categorias, writer)
        writer.close()
    }
    private fun saveNotas(notas: List<Nota>) {
        val file = File(filesDir, "notas.json")
        val writer = file.writer()
        Gson().toJson(notas, writer)
        writer.close()
    }
}
