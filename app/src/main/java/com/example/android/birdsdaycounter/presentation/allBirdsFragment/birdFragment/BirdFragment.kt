package com.example.android.birdsdaycounter.presentation.allBirdsFragment.birdFragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.birdsdaycounter.databinding.FragmentBirdBinding
import com.example.android.birdsdaycounter.globalUse.MyApp
import com.example.android.birdsdaycounter.globalUse.MyFragmentParentClass
import kotlinx.coroutines.launch

class BirdFragment : MyFragmentParentClass() {

    private val args by navArgs<BirdFragmentArgs>()
    private lateinit var binding: FragmentBirdBinding
    private val viewModel: BirdViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewModel.leaveControlFlag = true
        viewModel.initBird(args.bird)
        binding = FragmentBirdBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClicks(view)
        setViews(view)
    }

    private fun setOnClicks(view: View) {
        binding.nameSaveBtn.setOnClickListener {
            viewModel.bird.value!!.name = binding.birdName.text.toString()
            it.visibility = View.GONE
            lifecycleScope.launch {
                val myBird = viewModel.bird.value
                MyApp.updateBird(myBird!!)
            }
        }

        binding.ageSaveBtn.setOnClickListener {
            viewModel.bird.value!!.age = binding.birdAge.text.toString()
            it.visibility = View.GONE
            lifecycleScope.launch {
                val myBird = viewModel.bird.value
                MyApp.updateBird(myBird!!)
            }
        }

        binding.genderSaveBtn.setOnClickListener {
            val id = binding.myRadioGroup.checkedRadioButtonId
            viewModel.bird.value!!.gender = view.findViewById<RadioButton>(id).text.toString()
            it.visibility = View.GONE
            lifecycleScope.launch {
                val myBird = viewModel.bird.value
                MyApp.updateBird(myBird!!)
            }

        }
    }

    private fun setViews(view: View) {
        binding.birdImg.setImageURI(Uri.parse(viewModel.bird.value!!.imgLocation))
        binding.birdAge.setText(viewModel.bird.value!!.age)
        binding.birdName.setText(viewModel.bird.value!!.name)
        when (viewModel.bird.value!!.gender) {
            "don't know" -> binding.radiodonotknow.isChecked = true
            "male" -> binding.radiomale.isChecked = true
            "female" -> binding.radiofemale.isChecked = true
        }
        binding.ageSaveBtn.visibility = View.GONE
        binding.genderSaveBtn.visibility = View.GONE
        binding.nameSaveBtn.visibility = View.GONE

        saveNewValues(view)
    }

    private fun saveNewValues(view: View) {
        binding.birdName.doOnTextChanged { text, start, before, count ->
            if (text != viewModel.bird.value!!.name) {
                binding.nameSaveBtn.visibility = View.VISIBLE
            } else if (text == viewModel.bird.value!!.name) {
                binding.nameSaveBtn.visibility = View.GONE
            }
        }

        binding.birdAge.doOnTextChanged { text, start, before, count ->

            if (text != viewModel.bird.value!!.age) {
                binding.ageSaveBtn.visibility = View.VISIBLE
            } else if (text == viewModel.bird.value!!.age) {
                binding.ageSaveBtn.visibility = View.GONE
            }

        }

        binding.myRadioGroup.setOnCheckedChangeListener { radioGroup, i ->
            val id = radioGroup.checkedRadioButtonId
            val gender = view.findViewById<RadioButton>(id).text
            if (!gender.equals(viewModel.bird.value!!.gender)) {
                binding.genderSaveBtn.visibility = View.VISIBLE
            } else binding.genderSaveBtn.visibility = View.GONE

        }
    }

    override fun onAttach(context: Context) {
        if (viewModel.leaveControlFlag) {
            try {
                findNavController().navigateUp()
            } catch (E: java.lang.Exception) {
            }
        }
        super.onAttach(context)
    }

}