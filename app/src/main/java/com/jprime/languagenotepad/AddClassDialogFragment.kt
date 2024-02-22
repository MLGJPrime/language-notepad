package com.jprime.languagenotepad

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.jprime.languagenotepad.dao.WordClassDao
import com.jprime.languagenotepad.database.LanguageDatabase
import com.jprime.languagenotepad.databinding.DialogAddClassBinding
import com.jprime.languagenotepad.entities.WordClass
import kotlinx.coroutines.launch

class AddClassDialogFragment : DialogFragment() {

    private var _binding: DialogAddClassBinding? = null
    private val binding get() = _binding!!

    // Access the WordClassDao directly
    private val wordClassDao: WordClassDao by lazy {
        LanguageDatabase.getDatabase(requireContext()).wordClassDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DialogAddClassBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etClassTranslation.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                addWordClass()
                true
            } else {
                false
            }
        }

        binding.imgDone.setOnClickListener {
            addWordClass()
        }
    }

    private fun addWordClass() {
        val className = binding.etClassName.text.toString().trim()
        val classTranslation = binding.etClassTranslation.text.toString().trim()

        if (className.isNotEmpty() && classTranslation.isNotEmpty()) {
            // Insert data into the database using a coroutine
            lifecycleScope.launch {
                val newWordClass =
                    WordClass(className = className, classTranslation = classTranslation)

                // Insert directly into the database
                val newWordClassId = wordClassDao.upsertWordClass(newWordClass)

                // Create a new instance of CreateClassFragment with the WordClass ID
                val createClassFragment = CreateClassFragment.newInstance(newWordClassId)

                // Use the replaceFragment function to navigate to the new fragment
                replaceFragment(
                    createClassFragment,
                    addToBackStack = true,
                    withTransition = true
                )

                // Dismiss the dialog
                dismiss()
            }
        } else {
            // Display a toast message if either field is empty
            Toast.makeText(
                requireContext(),
                "Both fields must be filled to add the WordClass",
                Toast.LENGTH_SHORT
            ).show()
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
                android.R.anim.slide_out_right,
                android.R.anim.slide_in_left
            )
        }

        fragmentTransaction.replace(R.id.fragmentContainer, fragment)

        if (addToBackStack) {
            fragmentTransaction.addToBackStack(fragment.javaClass.simpleName)
        }

        fragmentTransaction.commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
