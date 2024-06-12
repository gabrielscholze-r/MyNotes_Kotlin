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
import com.example.mynotes.databinding.FragmentFilteredlistBinding
import com.example.mynotes.model.DataStore
import com.example.mynotes.model.Nota
import com.example.mynotes.view.NoteAdapter
import com.example.mynotes.view.NoteFragment
import com.google.gson.Gson
import java.io.File

class FilteredNotesFragment : Fragment() {
    private lateinit var binding: FragmentFilteredlistBinding
    private lateinit var adapter: NoteAdapter

    companion object {
        private const val ARG_CATEGORIA_NOME = "categoria_nome"

        fun newInstance(categoriaNome: String): FilteredNotesFragment {
            val fragment = FilteredNotesFragment()
            val args = Bundle()
            args.putString(ARG_CATEGORIA_NOME, categoriaNome)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFilteredlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val categoriaNome = arguments?.getString(ARG_CATEGORIA_NOME) ?: ""
        val todasNotas = DataStore.notas.filter { it.categoria.nome == categoriaNome }

        adapter = NoteAdapter(todasNotas.toMutableList(),
            { position -> editNote(position) },
            { position -> deleteNote(position) }
        )

        binding.filteredList.layoutManager = LinearLayoutManager(requireContext())
        binding.filteredList.adapter = adapter
    }

    private fun editNote(position: Int) {
        val intent = Intent(requireContext(), EditCreateNote::class.java)
        intent.putExtra("position", position)
        startActivityForResult(intent, 2)
    }

    private fun deleteNote(id: Int) {
        val note = DataStore.getNoteByID(id)
        val index = DataStore.notas.indexOf(note)
        AlertDialog.Builder(requireContext()).run {
            setMessage("Tem certeza que deseja excluir esta nota?")
            setPositiveButton("Excluir") { _, _ ->

                DataStore.removeNota(id)
                saveNotas(DataStore.notas)
                adapter.removeNoteAt(index)
                Toast.makeText(requireContext(), "Nota '${note.titulo}' excluída com sucesso!!!", Toast.LENGTH_LONG).show()
            }
            setNegativeButton("Cancelar") { dialog, _ ->
                adapter.notifyItemChanged(index)
                dialog.dismiss()
            }
            show()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            data?.let { intent ->
                val position = intent.getIntExtra("position", -1)
                if (position != -1) {
                    setupRecyclerView()
                }
            }
        }
    }
    private fun saveNotas(notas: List<Nota>) {
        val file = File(requireContext().filesDir, "notas.json")
        val writer = file.writer()
        Gson().toJson(notas, writer)
        writer.close()
    }
}
