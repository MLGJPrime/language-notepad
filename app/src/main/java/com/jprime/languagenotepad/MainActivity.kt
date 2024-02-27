package com.jprime.languagenotepad

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.jprime.languagenotepad.adapters.PairAdapter

class MainActivity : AppCompatActivity(), PairAdapter.OnItemListener {

    private val pairAdapter: PairAdapter = PairAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Replace the default fragment with ClassesFragment
        replaceFragment(ClassesFragment(), addToBackStack = false, withTransition = false)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val deleteBar = findViewById<View>(R.id.deleteBar)
        val headerLayout = findViewById<View>(R.id.headerLayout)
        val searchBar = findViewById<View>(R.id.searchBar)
        val svPairs = findViewById<SearchView>(R.id.svPairs)
        val btnAddNewPaid = findViewById<View>(R.id.btnAddNewPair)

        if (deleteBar.visibility == View.VISIBLE) {
            // If deleteBar is visible, hide it and show the headerLayout
            pairAdapter.setDeleteActionActive(false)
            deleteBar.visibility = View.GONE
            headerLayout.visibility = View.VISIBLE
            btnAddNewPaid.visibility = View.VISIBLE
        } else if (searchBar.visibility == View.VISIBLE) {
            svPairs.setQuery("", false)
            searchBar.visibility = View.GONE
            headerLayout.visibility = View.VISIBLE
            btnAddNewPaid.visibility = View.VISIBLE
        } else {
            // If deleteBar is not visible, perform default back press behavior
            super.onBackPressed()
        }
    }

    private fun replaceFragment(
        fragment: Fragment,
        addToBackStack: Boolean,
        withTransition: Boolean,
    ) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()

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

    override fun onItemFocusChanged(position: Int) {
    }

    override fun onEditorAction(position: Int) {
    }
}