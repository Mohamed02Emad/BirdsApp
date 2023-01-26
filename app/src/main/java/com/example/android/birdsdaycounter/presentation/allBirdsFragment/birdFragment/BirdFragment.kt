package com.example.android.birdsdaycounter.presentation.allBirdsFragment.birdFragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.activity.result.contract.ActivityResultContracts
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

        binding.saveBtn.setOnClickListener {
            viewModel.bird.value!!.name = binding.birdName.text.toString()
            viewModel.bird.value!!.age = binding.birdAge.text.toString()
            val id = binding.myRadioGroup.checkedRadioButtonId
            viewModel.bird.value!!.gender = view.findViewById<RadioButton>(id).text.toString()
            viewModel.changePic(binding.birdImg.drawable)
            it.visibility = View.GONE
            lifecycleScope.launch {
                val myBird = viewModel.bird.value
                MyApp.updateBird(myBird!!)
            }
        }

        binding.cameraIcon.setOnClickListener {

            val i = Intent().apply {
                setType("image/*")
                setAction(Intent.ACTION_GET_CONTENT)
            }
            resultLauncher.launch(i)
        }

        binding.backBtn.setOnClickListener { findNavController().navigateUp() }
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
        binding.saveBtn.visibility = View.GONE
        saveNewValues(view)
    }

    private fun saveNewValues(view: View) {
        binding.birdName.doOnTextChanged { text, start, before, count ->
           compareValues(view)
        }
        binding.birdAge.doOnTextChanged { text, start, before, count ->
            compareValues(view)
        }
        binding.myRadioGroup.setOnCheckedChangeListener { radioGroup, i ->
            compareValues(view)
        }
        viewModel.uri.observe(viewLifecycleOwner){
            if (viewModel.uri.value!=null)
            compareValues(view)
        }
    }

    fun compareValues(view: View) {
        val id = binding.myRadioGroup.checkedRadioButtonId
        val gender = view.findViewById<RadioButton>(id).text
        if (binding.birdAge.text.toString() != viewModel.bird.value!!.age ||
            binding.birdName.text.toString() != viewModel.bird.value!!.name ||
            viewModel.imageCheck()||
            !gender.equals(viewModel.bird.value!!.gender )
        ) {
            binding.saveBtn.visibility = View.VISIBLE
        } else {
            binding.saveBtn.visibility = View.GONE
        }
    }

    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                viewModel.uri.value = data!!.data
                binding.birdImg.setImageURI(viewModel.uri.value)
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