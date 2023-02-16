package com.example.android.birdsdaycounter.presentation

import android.app.Activity.RESULT_OK
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.BitmapCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.viewModelScope
import com.example.android.birdsdaycounter.R
import com.example.android.birdsdaycounter.data.models.Bird
import com.example.android.birdsdaycounter.databinding.AddBirdDialogBinding
import com.example.android.birdsdaycounter.globalUse.MyApp
import com.example.android.birdsdaycounter.presentation.allBirdsFragment.AllBirdsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream


class AddBirdDialog(private val viewModel: AllBirdsViewModel) : DialogFragment() {

    private var camUri: Uri? = null
    private lateinit var binding: AddBirdDialogBinding
    private var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().setFinishOnTouchOutside(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AddBirdDialogBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClicks(view)
    }

    private fun setOnClicks(view: View) {

        binding.cancelButton.setOnClickListener {
            this.dismiss()
        }

        binding.addBirdCamera.setOnClickListener {
            showBottomSheet()
        }

        binding.saveButton.setOnClickListener {

            val name = binding.birdNameET.text.toString()
            val age = binding.birdAgeET.text.toString()
            val id = binding.myRadioGroup.checkedRadioButtonId
            val gender = view.findViewById<RadioButton>(id).text.toString()
            val imgBitmap = binding.birdCreatImg.drawable.toBitmap()

            GlobalScope.launch(Dispatchers.IO) {
                val bytes = ByteArrayOutputStream()
                var bitmapByteCount = BitmapCompat.getAllocationByteCount(imgBitmap)
                if (bitmapByteCount>11*1227664){
                    imgBitmap.compress(Bitmap.CompressFormat.JPEG, 60, bytes)
                }else {
                    imgBitmap.compress(Bitmap.CompressFormat.PNG, 60, bytes)
                }
                val bird = Bird(age, name, gender, null)
                saveBird(bird, bytes)
            }
            this.dismiss()
        }
    }

    private fun saveBird(bird: Bird, byte: ByteArrayOutputStream) {
        viewModel.viewModelScope.launch {
            viewModel.saveBirdImage(bird, byte)
            viewModel.insertDB(bird)
            viewModel.newBirdWasAdded.value = true
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
        camUri = requireContext().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values
        )
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, camUri)

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
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                uri = data!!.data
                binding.birdCreatImg.setImageURI(uri)
            }
        }

    private val cameraResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                if (camUri != null)
                    binding.birdCreatImg.setImageURI(camUri)
            }
        }


    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
    }
}