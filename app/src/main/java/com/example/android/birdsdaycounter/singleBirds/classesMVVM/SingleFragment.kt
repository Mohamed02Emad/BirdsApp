package com.example.android.birdsdaycounter.singleBirds.classesMVVM

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.android.birdsdaycounter.databinding.FragmentSingleBinding
import com.example.android.birdsdaycounter.singleBirds.classesMVVM.SingleBirdViewModel

class SingleFragment : Fragment() {

    private lateinit var binding: FragmentSingleBinding
    private val viewModel: SingleBirdViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentSingleBinding.inflate(layoutInflater)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.addCollectionButton.setOnClickListener {
            viewModel.addNewCollection()
        }
    }

}