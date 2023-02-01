package com.example.android.birdsdaycounter.globalUse

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.android.birdsdaycounter.MainActivity
import com.example.android.birdsdaycounter.R

open class MyFragmentParentClass : Fragment() {
    // class that has functions that are used in more than one fragment


      var myparentFragment : Fragment? = null

    fun showToast(s: String) {
        Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show()
    }

    fun setFragment(fromFragment: Fragment, toFragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .hide(fromFragment)
            .add(R.id.frame_layout,toFragment)
            .addToBackStack(null)
            .setReorderingAllowed(true)
            .show(toFragment)
            .commit()

    }

    fun hideBottomBave(wantToHide: Boolean) {
        MainActivity.hideBottomNav(wantToHide)
    }

    fun popOfBackStack() {
        if (myparentFragment!=null){
        requireActivity().supportFragmentManager.popBackStack()
        requireActivity().supportFragmentManager.beginTransaction()
            .show(myparentFragment!!)
            .commit()
        }else{
            showToast("مبروك انت في القاع")
        }
    }

}