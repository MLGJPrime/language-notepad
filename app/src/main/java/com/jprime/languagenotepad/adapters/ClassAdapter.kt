package com.jprime.languagenotepad.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jprime.languagenotepad.ClassesFragment
import com.jprime.languagenotepad.R
import com.jprime.languagenotepad.database.LanguageDatabase
import com.jprime.languagenotepad.databinding.ItemClassBinding
import com.jprime.languagenotepad.entities.WordClass
import com.jprime.languagenotepad.entities.WordPair
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("NotifyDataSetChanged")

class ClassAdapter(private val classesFragment: ClassesFragment) :
    RecyclerView.Adapter<ClassAdapter.ClassViewHolder>() {

    private var classList = ArrayList<WordClass>()
    private var readPairList = ArrayList<WordPair>()
    private var selectedItems = HashSet<Long>()
    private var deleteActionActive: Boolean = false

    inner class ClassViewHolder(binding: ItemClassBinding) : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {

        val tvClassName: TextView = binding.tvClassName
        val tvClassTranslation: TextView = binding.tvClassTranslation
        val rvPairs: RecyclerView = binding.rvPairs
        private val cardView: CardView = binding.cardView
        private val linearLayout: LinearLayout = binding.linearLayout
        val imgSelect: ImageView = binding.imgSelect

        init {
            Log.d("ClassAdapter", "Init block called for position: $bindingAdapterPosition")
            linearLayout.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val clickedClass = classList[position]
                classesFragment.onClassClicked(clickedClass.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
        val binding = ItemClassBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClassViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        val wordClass = classList[position]

        Log.d(
            "ClassAdapter",
            "Binding WordClass: ${wordClass.className}, ${wordClass.classTranslation}"
        )

        // Set selected state
        holder.imgSelect.setImageResource(
            if (selectedItems.contains(wordClass.id)) R.drawable.ic_selected
            else R.drawable.ic_unselected
        )

        holder.imgSelect.visibility = if (deleteActionActive) View.VISIBLE else View.GONE

        // Toggle selection on click
        holder.itemView.setOnClickListener {
            if (selectedItems.contains(wordClass.id)) {
                selectedItems.remove(wordClass.id)
                Log.d("PairAdapter", "Item deselected with ID: ${wordClass.id}")
            } else {
                selectedItems.add(wordClass.id)
                Log.d("PairAdapter", "Item selected with ID: ${wordClass.id}")
            }
            notifyDataSetChanged()
        }

        holder.tvClassName.text = wordClass.className
        holder.tvClassTranslation.text = wordClass.classTranslation

        holder.rvPairs.setHasFixedSize(true)
        holder.rvPairs.layoutManager = LinearLayoutManager(holder.itemView.context)

        val classId = wordClass.id

        classesFragment.lifecycleScope.launch(Dispatchers.IO) {
            holder.itemView.context?.let {
                val wordPairs = LanguageDatabase.getDatabase(it).wordPairDao()
                    .getWordPairsByClassId(classId)

                Log.d(
                    "ClassAdapter",
                    "WordPairs count for ${wordClass.className}: ${wordPairs.size}"
                )

                withContext(Dispatchers.Main) {
                    val readPairsAdapter = ReadPairsAdapter()

                    val firstTenPairs = wordPairs.take(10)

                    readPairsAdapter.setData(firstTenPairs)

                    Log.d(
                        "ClassAdapter",
                        "ReadPairsAdapter count after setData: ${readPairsAdapter.itemCount}"
                    )

                    readPairList = wordPairs as ArrayList<WordPair>
                    holder.rvPairs.adapter = readPairsAdapter
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return classList.size
    }

    fun setData(data: List<WordClass>) {
        classList = data as ArrayList<WordClass>

        Log.d("ClassAdapter", "Data updated. New item count: ${data.size}")
    }

    fun setDeleteActionActive(isActive: Boolean) {
        deleteActionActive = isActive
        notifyDataSetChanged()
    }

    fun getSelectedItems(): HashSet<Long> {
        return selectedItems
    }

    fun toggleSelectAll(): Boolean {
        if (selectedItems.size == classList.size) {
            selectedItems.clear()
        } else {
            selectedItems.addAll(classList.map { it.id })
        }
        notifyDataSetChanged()
        return selectedItems.isEmpty()
    }
}