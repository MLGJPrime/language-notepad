package com.jprime.languagenotepad.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jprime.languagenotepad.databinding.ItemTvWordPairBinding
import com.jprime.languagenotepad.entities.WordPair

class ReadPairsAdapter : RecyclerView.Adapter<ReadPairsAdapter.ReadPairsViewHolder>() {

    private var readPairList = ArrayList<WordPair>()

    inner class ReadPairsViewHolder(binding: ItemTvWordPairBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val tvWord: TextView = binding.tvWord
        val tvTranslation: TextView = binding.tvTranslation
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReadPairsViewHolder {
        val binding = ItemTvWordPairBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ReadPairsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReadPairsViewHolder, position: Int) {
        val wordPair = readPairList[position]
        holder.tvWord.text = wordPair.word
        holder.tvTranslation.text = wordPair.translation

        // Log onBindViewHolder
        Log.d("ReadPairsAdapter", "onBindViewHolder called for position: $position")
    }

    override fun getItemCount(): Int {
        return readPairList.size
    }

    fun setData(data: List<WordPair>) {
        readPairList = if (data.isNotEmpty()) {
            ArrayList(data)
        } else {
            readPairList.apply { clear() }
        }
    }
}
