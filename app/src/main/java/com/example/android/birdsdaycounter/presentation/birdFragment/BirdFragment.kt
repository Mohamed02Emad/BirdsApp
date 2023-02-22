package com.example.android.birdsdaycounter.presentation.birdFragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.birdsdaycounter.R
import com.example.android.birdsdaycounter.databinding.FragmentBirdBinding
import com.example.android.birdsdaycounter.globalUse.MyFragmentParentClass
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch

class BirdFragment : MyFragmentParentClass() {


    private lateinit var binding: FragmentBirdBinding
    private val viewModel: BirdViewModel by viewModels()
    private val args by navArgs<BirdFragmentArgs>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewModel.initBird(args.bird)
        binding = FragmentBirdBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClicks(view)
        setViews(view)
    }

    @SuppressLint("ClickableViewAccessibility")
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
                viewModel.updateDB(myBird!!)
            }

      findNavController().navigateUp()
        }

        binding.deleteBtn.setOnClickListener {

            //todo : add logic
            findNavController().navigateUp()
        }

        binding.scroll.setOnTouchListener(View.OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {

                if (binding.birdName.isFocused) {
                    val outRect = Rect()
                    binding.birdName.getGlobalVisibleRect(outRect)
                    if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                        binding.birdName.clearFocus()
                        val imm: InputMethodManager =
                            v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(v.windowToken, 0)
                    }
                }

                if (binding.birdAge.isFocused) {
                    val outRect = Rect()
                    binding.birdAge.getGlobalVisibleRect(outRect)
                    if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                        binding.birdAge.clearFocus()
                        val imm: InputMethodManager =
                            v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(v.windowToken, 0)
                    }
                }
            }
            false
        })



        binding.cameraIcon.setOnClickListener {

           showBottomSheet()
        }

        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
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
        binding.saveBtn.visibility = View.GONE
        saveNewValues(view)
    }

    private fun saveNewValues(view: View) {
        binding.birdName.doOnTextChanged { text, start, before, count ->
            if ( binding.birdName.text.toString() == viewModel.bird.value!!.name) {
                binding.saveBtn.visibility=View.GONE
            }
        }
        binding.birdName.setOnFocusChangeListener { et, isClicked ->
            if (!isClicked){
                compareValuesToSave(view)
            }
        }
        binding.birdAge.doOnTextChanged { text, start, before, count ->
            compareValuesToSave(view)
        }
        binding.myRadioGroup.setOnCheckedChangeListener { radioGroup, i ->
            compareValuesToSave(view)
        }
        viewModel.uri.observe(viewLifecycleOwner){
            if (viewModel.imageCheck())
                compareValuesToSave(view)
        }
    }

    private fun compareValuesToSave(view: View) {
        val id = binding.myRadioGroup.checkedRadioButtonId
        val gender = view.findViewById<RadioButton>(id).text
        if (binding.birdAge.text.toString() != viewModel.bird.value!!.age ||
            binding.birdName.text.toString() != viewModel.bird.value!!.name ||
            viewModel.imageCheck()||
            !gender.equals(viewModel.bird.value!!.gender )
        ) {
            binding.saveBtn.visibility = View.VISIBLE

            if (viewModel.firstTimeChange.value == true) {
                binding.scroll.postDelayed({
                    binding.scroll.scrollTo(
                        0,
                        binding.saveBtn.y.toInt()
                    )
                }, 100)
                viewModel.setFirstTimeChange(false)
            }

        } else {
            binding.saveBtn.visibility = View.GONE
            viewModel.setFirstTimeChange(true)
        }
    }

    private fun showBottomSheet() {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.chose_edit_image, null)
        val btnCamera: LinearLayout = view.findViewById(R.id.camera_choice)
        btnCamera.setOnClickListener {
            startCameraIntent()
            dialog.dismiss()
        }
        val btnGallery: LinearLayout = view.findViewById(R.id.gallery_choice)
        btnGallery.setOnClickListener {
            startGalleryIntent()
            dialog.dismiss()
        }
        dialog.setCancelable(true)
        dialog.setContentView(view)
        dialog.show()
    }

    private fun startCameraIntent() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera")
        viewModel.uri.value = requireContext().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values
        )
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, viewModel.uri.value)

        cameraResultLauncher.launch(cameraIntent)
    }

    private fun startGalleryIntent() {
        val i = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        galleryResultLauncher.launch(i)
    }

    private val galleryResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                viewModel.uri.value = data!!.data
                binding.birdImg.setImageURI(viewModel.uri.value)
            }
        }

    private val cameraResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == Activity.RESULT_OK) {
                if (viewModel.uri.value != null)
                    binding.birdImg.setImageURI(viewModel.uri.value)
            }
        }


}