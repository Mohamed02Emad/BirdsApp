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

//    fun setFragment(fromFragment : Fragment, toFragment: Fragment) {
//        requireActivity().supportFragmentManager.beginTransaction()
//            .add(R.id.frame_layout,toFragment)
//            .addToBackStack(null)
//            .hide(fromFragment)
//            .setReorderingAllowed(true)
//            .commit()
//    }
//
//    fun hideBottomBave(wantToHide: Boolean) {
//        MainActivity.hideBottomNav(wantToHide)
//    }
//
//    fun popOfBackStack() {
//        hideBottomBave(false)
//        requireActivity().supportFragmentManager.popBackStack()
//    }
//
//    fun showFragment(fragment: Fragment){
//        requireActivity().supportFragmentManager.beginTransaction()
//            .show(fragment)
//            .commit()
//    }

}