package com.jprime.languagenotepad

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jprime.languagenotepad.adapters.PairAdapter
import com.jprime.languagenotepad.dao.WordClassDao
import com.jprime.languagenotepad.database.LanguageDatabase
import com.jprime.languagenotepad.databinding.FragmentCreateClassBinding
import com.jprime.languagenotepad.entities.WordPair
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("NotifyDataSetChanged")
class CreateClassFragment : Fragment(), PairAdapter.OnItemListener {

    private val pairAdapter: PairAdapter = PairAdapter(this)
    private var _binding: FragmentCreateClassBinding? = null
    private val binding get() = _binding!!
    private var pairList = ArrayList<WordPair>()
    private var focusedPosition: Int = RecyclerView.NO_POSITION

    // Access the WordClassDao directly
    private val wordClassDao: WordClassDao by lazy {
        LanguageDatabase.getDatabase(requireContext()).wordClassDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCreateClassBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        // Modify the newInstance method to accept the wordClassId parameter
        @JvmStatic
        fun newInstance(wordClassId: Long) = CreateClassFragment().apply {
            arguments = Bundle().apply {
                putLong(ARG_WORD_CLASS_ID, wordClassId)
            }
        }

        // Define the constant for the argument
        private const val ARG_WORD_CLASS_ID = "WORD_CLASS_ID"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val classId = arguments?.getLong(ARG_WORD_CLASS_ID, -1) ?: -1

        // Use lifecycleScope to launch a coroutine within the lifecycle of the Fragment
        viewLifecycleOwner.lifecycleScope.launch {
            val wordClass = wordClassDao.getWordClass(classId)

            // Update UI with the retrieved WordClass data
            if (wordClass != null) {
                binding.tvClassName.text = wordClass.className
                binding.tvClassTranslation.text = wordClass.classTranslation
            }
        }

        binding.rvPairs.setHasFixedSize(true)

        binding.rvPairs.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch(Dispatchers.IO) {
            context?.let {
                val wordPairs =
                    LanguageDatabase.getDatabase(it).wordPairDao().getWordPairsByClassId(classId)
                withContext(Dispatchers.Main) {
                    pairAdapter.setData(wordPairs)
                    pairList = wordPairs as ArrayList<WordPair>
                    binding.rvPairs.adapter = pairAdapter
                }
            }
        }

        binding.imgBack.setOnClickListener {
            // Get the FragmentManager
            val fragmentManager = requireActivity().supportFragmentManager

            // Check if there's anything in the back stack
            if (fragmentManager.backStackEntryCount > 0) {
                // If there's something in the back stack, pop it
                fragmentManager.popBackStack()
            } else {
                // If the back stack is empty, you may want to handle this case
                // For example, go back to a certain activity or perform some other action
            }
        }

        binding.btnAddNewPair.setOnClickListener {
            addNewPair(classId)
        }

        binding.imgDone.setOnClickListener {
            binding.imgDone.visibility = View.GONE
            binding.imgMore.visibility = View.VISIBLE

            view.findFocus()?.clearFocus()
            activity?.hideKeyboard()
        }

        binding.imgMore.setOnClickListener { imgMoreView ->
            // Create a PopupMenu
            val wrapper = ContextThemeWrapper(requireContext(), R.style.PopupMenuStyle)
            val popupMenu = PopupMenu(wrapper, imgMoreView)

            // Set custom style
            popupMenu.menu.javaClass.getDeclaredMethod(
                "setOptionalIconsVisible", Boolean::class.javaPrimitiveType
            ).apply {
                isAccessible = true
                invoke(popupMenu.menu, true)
            }

            // Inflate the menu XML file
            popupMenu.menuInflater.inflate(R.menu.menu_options_create_class, popupMenu.menu)

            // Set item click listener for the menu items
            popupMenu.setOnMenuItemClickListener { menuItem ->
                // Handle menu item clicks
                when (menuItem.itemId) {
                    R.id.action_search -> {
                        // Handle search action
                        binding.headerLayout.visibility = View.GONE
                        binding.searchBar.root.visibility = View.VISIBLE
                        binding.btnAddNewPair.visibility = View.GONE
                        true
                    }

                    R.id.action_delete -> {
                        // Handle delete action
                        binding.headerLayout.visibility = View.GONE
                        binding.deleteBar.root.visibility = View.VISIBLE
                        binding.btnAddNewPair.visibility = View.GONE
                        pairAdapter.setDeleteActionActive(true)
                        true
                    }

                    R.id.action_settings -> {
                        // Handle settings action
                        true
                    }

                    else -> false
                }
            }

            // Show the PopupMenu
            popupMenu.show()
        }

        binding.searchBar.svPairs.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredPairs = pairList.filter { pair ->
                    pair.word.contains(newText.orEmpty(), ignoreCase = true) ||
                            pair.translation.contains(newText.orEmpty(), ignoreCase = true)
                }
                pairAdapter.setData(filteredPairs)
                pairAdapter.notifyDataSetChanged()
                binding.btnAddNewPair.visibility = View.VISIBLE
                return true
            }
        })

        binding.searchBar.imgClear.setOnClickListener {
            // Clear focus from the search view
            binding.searchBar.svPairs.clearFocus()

            // Hide the search bar
            binding.searchBar.root.visibility = View.GONE

            // Show the header layout
            binding.headerLayout.visibility = View.VISIBLE
            binding.btnAddNewPair.visibility = View.VISIBLE

        }

        binding.deleteBar.imgDelete.setOnClickListener {
            // Get the list of selected items from the PairAdapter
            val selectedItems = pairAdapter.getSelectedItems()

            // Delete the selected items from the database
            deleteSelectedItems(selectedItems)
            pairAdapter.setDeleteActionActive(false)
            // Hide the search bar
            binding.deleteBar.root.visibility = View.GONE

            // Show the header layout
            binding.headerLayout.visibility = View.VISIBLE
            binding.btnAddNewPair.visibility = View.VISIBLE

        }
    }


    private fun deleteSelectedItems(selectedItems: HashSet<Long>) {
        lifecycleScope.launch(Dispatchers.IO) {
            context?.let { ctx ->
                val wordPairDao = LanguageDatabase.getDatabase(ctx).wordPairDao()
                withContext(Dispatchers.Main) {
                    selectedItems.forEach { id ->
                        wordPairDao.deleteWordPair(id)
                    }

                    pairList.removeAll { it.id in selectedItems }
                    pairAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun updateItemList(classId: Long) {
        lifecycleScope.launch(Dispatchers.IO) {
            context?.let {
                val wordPairs =
                    LanguageDatabase.getDatabase(it).wordPairDao().getWordPairsByClassId(classId)
                withContext(Dispatchers.Main) {
                    for (position in 0 until (pairList.size)) {
                        val updatedWordPair = pairList[position]
                        val storedWordPair = wordPairs.getOrNull(position)
                        if (!areWordPairsEqual(storedWordPair, updatedWordPair)) {
                            // Check if storedWordPair has a non-null ID
                            if (storedWordPair != null) {
                                // Update the ID of updatedWordPair to match storedWordPair
                                updatedWordPair.id = storedWordPair.id
                            }
                            Log.d(
                                "UpdateItemList",
                                "Updating pair from $storedWordPair to $updatedWordPair at position: $position"
                            )
                            // Update the entry in the database
                            LanguageDatabase.getDatabase(it).wordPairDao()
                                .upsertWordPair(updatedWordPair)
                        }
                    }
                }
            }
        }
    }

    private fun addNewPair(classId: Long) {
        val newWordPair = WordPair(classId = classId)

        lifecycleScope.launch(Dispatchers.IO) {
            context?.let {
                val wordPairs =
                    LanguageDatabase.getDatabase(it).wordPairDao().getWordPairsByClassId(classId)

                withContext(Dispatchers.Main) {
                    pairAdapter.setData(wordPairs)
                    pairList = wordPairs as ArrayList<WordPair>
                    pairList.add(newWordPair)
                    pairAdapter.notifyItemInserted(pairList.size - 1)
                    binding.rvPairs.scrollToPosition(pairList.size - 1)
                }
            }
        }
    }

    private fun areWordPairsEqual(wordPair1: WordPair?, wordPair2: WordPair): Boolean {
        return wordPair1?.let {
            it.word == wordPair2.word && it.translation == wordPair2.translation
        } ?: false
    }

    override fun onItemFocusChanged(position: Int) {
        // Update visibility of imgDone and imgMore based on the focused item
        // Set visibility based on your conditions
        binding.imgDone.visibility = View.VISIBLE
        binding.imgMore.visibility = View.GONE
//        binding.btnAddNewPair.visibility = View.GONE

        focusedPosition = position
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onEditorAction(position: Int) {
        focusedPosition = position
        val classId = arguments?.getLong(ARG_WORD_CLASS_ID, -1) ?: -1
        updateItemList(classId)
    }

    private fun Activity.hideKeyboard() {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = this.currentFocus ?: window.decorView.rootView
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}