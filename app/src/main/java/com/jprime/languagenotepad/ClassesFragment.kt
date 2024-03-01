package com.jprime.languagenotepad

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.jprime.languagenotepad.adapters.ClassAdapter
import com.jprime.languagenotepad.database.LanguageDatabase
import com.jprime.languagenotepad.databinding.FragmentClassesBinding
import com.jprime.languagenotepad.entities.WordClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("NotifyDataSetChanged")
class ClassesFragment : Fragment() {

    private val classAdapter: ClassAdapter = ClassAdapter(this)
    private var _binding: FragmentClassesBinding? = null
    private val binding get() = _binding!!
    private var classList = ArrayList<WordClass>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentClassesBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = ClassesFragment().apply {
            arguments = Bundle().apply {}
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fbCreateClass.setOnClickListener {
            val addClassDialog = AddClassDialogFragment()

            // Set the window background to transparent
            addClassDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.TransparentDialog)

            addClassDialog.show(parentFragmentManager, "AddClassDialog")
        }

        binding.rvClasses.setHasFixedSize(true)

        binding.rvClasses.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        lifecycleScope.launch(Dispatchers.IO) {
            context?.let {
                val wordClasses =
                    LanguageDatabase.getDatabase(it).wordClassDao().getAllWordClasses()
                withContext(Dispatchers.Main) {
                    classAdapter.setData(wordClasses)
                    classList = wordClasses as ArrayList<WordClass>
                    binding.rvClasses.adapter = classAdapter
                }
            }
        }

        binding.svClasses.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredPairs = classList.filter { item ->
                    item.className!!.contains(newText.orEmpty(), ignoreCase = true) ||
                            item.classTranslation!!.contains(newText.orEmpty(), ignoreCase = true)
                }
                classAdapter.setData(filteredPairs)
                classAdapter.notifyDataSetChanged()
                return true
            }
        })

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
            popupMenu.menuInflater.inflate(R.menu.menu_options_classes, popupMenu.menu)

            // Set item click listener for the menu items
            popupMenu.setOnMenuItemClickListener { menuItem ->
                // Handle menu item clicks
                when (menuItem.itemId) {

                    R.id.action_delete -> {
                        // Handle delete action
                        binding.headerLayout.visibility = View.GONE
                        binding.deleteBar.root.visibility = View.VISIBLE
                        binding.fbCreateClass.visibility = View.GONE
                        classAdapter.setDeleteActionActive(true)
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

        binding.deleteBar.imgDelete.setOnClickListener {
            // Get the list of selected items from the PairAdapter
            val selectedItems = classAdapter.getSelectedItems()

            // Delete the selected items from the database
            deleteSelectedItems(selectedItems)
            classAdapter.setDeleteActionActive(false)
            // Hide the search bar
            binding.deleteBar.root.visibility = View.GONE

            // Show the header layout
            binding.headerLayout.visibility = View.VISIBLE
            binding.fbCreateClass.visibility = View.VISIBLE

        }

        binding.deleteBar.imgSelect.setOnClickListener {
            val isAllSelected = classAdapter.toggleSelectAll()
            binding.deleteBar.imgSelect.setImageResource(
                if (isAllSelected) R.drawable.ic_unselected
                else R.drawable.ic_selected
            )
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

                    classList.removeAll { it.id in selectedItems }
                    classAdapter.notifyDataSetChanged()
                }
            }
        }
    }


    private fun replaceFragment(
        fragment: Fragment,
        addToBackStack: Boolean,
        withTransition: Boolean,
    ) {
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()

        if (withTransition) {
            fragmentTransaction.setCustomAnimations(
                android.R.anim.slide_out_right, android.R.anim.slide_in_left
            )
        }

        fragmentTransaction.replace(R.id.fragmentContainer, fragment)

        if (addToBackStack) {
            fragmentTransaction.addToBackStack(fragment.javaClass.simpleName)
        }

        fragmentTransaction.commit()
    }

    fun onClassClicked(classId: Long) {
        val createClassFragment = CreateClassFragment.newInstance(classId)
        replaceFragment(createClassFragment, addToBackStack = true, withTransition = true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
