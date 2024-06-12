package com.example.mynotes.controller

import CategoriasFragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.mynotes.R
import com.example.mynotes.databinding.ActivityMainBinding
import com.example.mynotes.model.Categoria
import com.example.mynotes.model.DataStore
import com.example.mynotes.view.NoteFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        copyJsonToInternalStorage()
        DataStore.categorias=loadCategoriasFromInternalStorage().toMutableList()

        loadFragment(NoteFragment())

        val bottomNavigationView: BottomNavigationView = binding.bottomNavigation
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_notes -> loadFragment(NoteFragment())
                R.id.nav_item3 -> loadFragment(CategoriasFragment())
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(binding.frameLayout.id, fragment).commit()
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
}
