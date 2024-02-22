package com.jprime.languagenotepad.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.jprime.languagenotepad.R
import com.jprime.languagenotepad.databinding.ItemWordPairBinding
import com.jprime.languagenotepad.entities.WordPair

@SuppressLint("NotifyDataSetChanged")
class PairAdapter(private val onItemListener: OnItemListener) :
    RecyclerView.Adapter<PairAdapter.PairViewHolder>() {

    private var pairList = ArrayList<WordPair>()
    private var selectedItems = HashSet<Long>()
    private var deleteActionActive: Boolean = false

    interface OnItemListener {
        fun onItemFocusChanged(position: Int)
        fun onEditorAction(position: Int)
    }

    inner class PairViewHolder(binding: ItemWordPairBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val etWord: EditText = binding.etWord
        val etTranslation: EditText = binding.etTranslation
        val imgSelect: ImageView = binding.imgSelect
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PairViewHolder {
        val binding =
            ItemWordPairBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val viewHolder = PairViewHolder(binding)

        viewHolder.etWord.imeOptions = EditorInfo.IME_ACTION_NEXT

        viewHolder.etTranslation.imeOptions = EditorInfo.IME_ACTION_DONE

        return viewHolder
    }

    override fun onBindViewHolder(holder: PairViewHolder, position: Int) {
        val wordPair = pairList[position]

        Log.d("PairAdapter", "Current selected items: $selectedItems")

        // Set text from WordPair
        holder.etWord.setText(wordPair.word)
        holder.etTranslation.setText(wordPair.translation)

        // Set selected state
        holder.imgSelect.setImageResource(
            if (selectedItems.contains(wordPair.id)) R.drawable.ic_selected
            else R.drawable.ic_unselected
        )

        holder.imgSelect.visibility = if (deleteActionActive) View.VISIBLE else View.GONE

        // Toggle selection on click
        holder.itemView.setOnClickListener {
            if (selectedItems.contains(wordPair.id)) {
                selectedItems.remove(wordPair.id)
                Log.d("PairAdapter", "Item deselected with ID: ${wordPair.id}")
            } else {
                selectedItems.add(wordPair.id)
                Log.d("PairAdapter", "Item selected with ID: ${wordPair.id}")
            }
            notifyDataSetChanged()
        }

        // Update WordPair data when EditText values change
        holder.etWord.addTextChangedListener {
            pairList[position] = pairList[position].copy(word = it.toString())
        }

        holder.etTranslation.addTextChangedListener {
            pairList[position] = pairList[position].copy(translation = it.toString())
        }

        // Add focus change listener
        holder.etWord.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                onItemListener.onItemFocusChanged(position)
            }
        }

        holder.etWord.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                // Manually handle "Next" action by focusing etTranslation
                holder.etTranslation.requestFocus()
                true
            } else {
                false
            }
        }

        holder.etTranslation.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                onItemListener.onItemFocusChanged(position)
            }
        }

        holder.etTranslation.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Save the WordPair when Enter key is pressed in etTranslation
                if (holder.etWord.text.isNotEmpty() && holder.etTranslation.text.isNotEmpty()) {
                    onItemListener.onEditorAction(position)
                } else {
                    // Display a toast message if either field is empty
                    Toast.makeText(
                        holder.itemView.context,
                        "Both fields must be filled",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                true
            } else {
                false
            }
        }
    }

    fun setData(data: List<WordPair>) {
        pairList = data as ArrayList<WordPair>
    }

    fun setDeleteActionActive(isActive: Boolean) {
        deleteActionActive = isActive
        notifyDataSetChanged() // Notify RecyclerView to refresh views
    }

    fun getSelectedItems(): List<WordPair> {
        return pairList.filter { selectedItems.contains(it.id) }
    }

    override fun getItemCount(): Int {
        return pairList.size
    }
}