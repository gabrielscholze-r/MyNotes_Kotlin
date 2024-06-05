import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mynotes.databinding.AdapterCategoriaBinding
import com.example.mynotes.model.Categoria

class CategoriasAdapter(
    private val categorias: List<Categoria>,
    private val onCategoriaClick: (String) -> Unit
) : RecyclerView.Adapter<CategoriasAdapter.CategoriasHolder>() {

    inner class CategoriasHolder(private val binding: AdapterCategoriaBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(categoria: Categoria) {
            binding.txtCategoria.text = categoria.nome
            binding.root.setOnClickListener {
                onCategoriaClick(categoria.nome)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriasHolder {
        val binding = AdapterCategoriaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CategoriasHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoriasHolder, position: Int) {
        val categoria = categorias[position]
        holder.bind(categoria)
    }

    override fun getItemCount(): Int = categorias.size
}
