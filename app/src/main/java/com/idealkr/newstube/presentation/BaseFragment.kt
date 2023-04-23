package com.idealkr.newstube.presentation

import android.view.View
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {
    protected open var bottomNavigationViewVisibility = View.GONE

    override fun onResume() {
        super.onResume()
        if (activity is MainActivity) {
            val mainActivity = activity as MainActivity
            mainActivity.setBottomNavigationVisibility(bottomNavigationViewVisibility)
        }
    }
}