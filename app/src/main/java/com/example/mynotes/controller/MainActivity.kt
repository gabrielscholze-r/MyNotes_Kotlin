package com.example.mynotes.controller

import android.content.Intent
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynotes.databinding.ActivityMainBinding
import com.example.mynotes.model.DataStore
import com.example.mynotes.model.Nota
import com.example.mynotes.view.NoteAdapter
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: NoteAdapter
    private lateinit var gestureDetector: GestureDetector

    private val addNoteForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.let { intent ->
                adapter.notifyItemInserted(DataStore.notas.size - 1)
                Snackbar.make(
                    binding.root,
                    "Nota adicionada com sucesso!!!",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private val editNoteForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.let { intent ->
                val position = intent.getIntExtra("position", -1)
                if (position != -1) {
                    adapter.notifyItemChanged(position)
                } else {
                    adapter.notifyItemInserted(DataStore.notas.size - 1)
                }
                Snackbar.make(
                    binding.root,
                    "Nota alterada com sucesso!!!",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadRecyclerView()
        configureFab()
        configureGesture()
        configureRecyclerViewEvents()
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this).run {
            setMessage("Tem certeza que deseja fechar esta tela?")
            setPositiveButton(getString(android.R.string.ok)) { _, _ ->
                super.onBackPressed()
            }
            setNegativeButton(getString(android.R.string.cancel), null)
            show()
        }
    }

    private fun loadRecyclerView() {
        val notes = DataStore.notas

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = NoteAdapter(notes){}
        binding.recyclerView.adapter = adapter
    }

    private fun configureGesture() {
        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                binding.recyclerView.findChildViewUnder(e.x, e.y).run {
                    this?.let { child ->
                        binding.recyclerView.getChildAdapterPosition(child).apply {
                            Intent(this@MainActivity, EditCreateNote::class.java).run {
                                putExtra("position", this@apply)
                                editNoteForResult.launch(this)
                            }
                        }
                    }
                }
                return super.onSingleTapConfirmed(e)
            }

            override fun onLongPress(e: MotionEvent) {
                binding.recyclerView.findChildViewUnder(e.x, e.y)?.let { child ->
                    val position = binding.recyclerView.getChildAdapterPosition(child)
                    val note = DataStore.getNota(position)
                    AlertDialog.Builder(this@MainActivity).run {
                        setMessage("Tem certeza que deseja remover esta nota?")
                        setPositiveButton("Excluir") { _, _ ->
                            DataStore.removeNota(position)
                            Toast.makeText(this@MainActivity, "Nota '${note.titulo}' removida com sucesso!!!", Toast.LENGTH_LONG).show()
                            adapter.notifyDataSetChanged()
                        }
                        setNegativeButton("Cancelar", null)
                        show()
                    }
                }
            }
        })
    }

    private fun configureRecyclerViewEvents() {
        binding.recyclerView.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                gestureDetector.onTouchEvent(e)
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })
    }

    private fun addNote() {
        Intent(this, EditCreateNote::class.java).run {
            addNoteForResult.launch(this)
        }
    }

    private fun configureFab() {
        binding.fab.setOnClickListener {
            addNote()
        }
    }
}
