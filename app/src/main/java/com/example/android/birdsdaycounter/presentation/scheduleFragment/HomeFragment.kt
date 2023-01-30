package com.example.android.birdsdaycounter.presentation.scheduleFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.birdsdaycounter.R
import com.example.android.birdsdaycounter.globalUse.MyFragmentParentClass
import com.example.android.birdsdaycounter.presentation.allBirdsFragment.AllBirdsFragment

class HomeFragment : MyFragmentParentClass() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }







    companion object{
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }
}