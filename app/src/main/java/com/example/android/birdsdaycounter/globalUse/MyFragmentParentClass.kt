package com.example.android.birdsdaycounter.globalUse

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.android.birdsdaycounter.MainActivity
import com.example.android.birdsdaycounter.R

open class MyFragmentParentClass : Fragment() {
    // class that has functions that are used in more than one fragment

    fun showToast(s: String) {
        Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show()
    }

    fun setFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment, "main activity")
            .addToBackStack(null)
            .setReorderingAllowed(true)
            .commit()
    }

    fun hideBottomBave(wantToHide : Boolean){
        MainActivity.hideBottomNav(wantToHide)
    }

    fun popOfBackStack() {
        requireActivity().supportFragmentManager.popBackStack()
    }

}