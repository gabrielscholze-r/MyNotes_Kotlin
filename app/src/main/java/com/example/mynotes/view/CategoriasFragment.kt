import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mynotes.R
import com.example.mynotes.databinding.FragmentCategoriasBinding
import com.example.mynotes.model.Categoria
import com.example.mynotes.model.DataStore

class CategoriasFragment : Fragment() {
    private lateinit var binding: FragmentCategoriasBinding
    private lateinit var adapter: CategoriasAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoriasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val categorias = DataStore.categorias

        adapter = CategoriasAdapter(categorias) { categoriaNome ->
            val fragment = FilteredNotesFragment.newInstance(categoriaNome)
            parentFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .addToBackStack(null)
                .commit()
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }
}
