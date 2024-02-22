package com.jprime.languagenotepad

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
