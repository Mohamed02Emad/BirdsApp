package com.example.android.birdsdaycounter.presentation.multiBirdsFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.birdsdaycounter.R
import com.example.android.birdsdaycounter.globalUse.MyFragmentParentClass

class MultiBirdFragment : MyFragmentParentClass() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_multi_bird, container, false)
    }

}