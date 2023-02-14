package com.example.android.birdsdaycounter.presentation

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.RadioButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.viewModelScope
import com.example.android.birdsdaycounter.data.models.Bird
import com.example.android.birdsdaycounter.databinding.AddBirdDialogBinding
import com.example.android.birdsdaycounter.presentation.allBirdsFragment.AllBirdsViewModel
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


class AddBirdDialog(private val viewModel: AllBirdsViewModel) : DialogFragment() {

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

            val i = Intent().apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
            }
            resultLauncher.launch(i)
        }

        binding.saveButton.setOnClickListener {
            val name = binding.birdNameET.text.toString()
            val age = binding.birdAgeET.text.toString()
            val id = binding.myRadioGroup.checkedRadioButtonId
            val gender = view.findViewById<RadioButton>(id).text.toString()

            val imgBitmap = binding.birdCreatImg.drawable.toBitmap()
            val bytes = ByteArrayOutputStream()
            imgBitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes)

            val bird = Bird(age, name, gender, null)

            saveBird(bird, bytes)
        }
    }

    private fun saveBird(bird: Bird, byte: ByteArrayOutputStream) {

        viewModel.viewModelScope.launch {
            saveBirdImage(bird, byte)
            viewModel.insertDB(bird)
            viewModel.newBirdWasAdded.value = true
        }
        this.dismiss()
    }

    private fun saveBirdImage(bird: Bird, byte: ByteArrayOutputStream) {
        val imgLocation = requireActivity().filesDir.absolutePath + File.separator
        val myAppDir = File(imgLocation)
        if (!myAppDir.exists()) myAppDir.mkdir()

        //creating child file
        val fileName = "${System.currentTimeMillis()}.png"
        val imageFile = File(myAppDir, fileName)
        if (!imageFile.exists()) imageFile.createNewFile()

        try {
            val fo = FileOutputStream(imageFile)
            fo.write(byte.toByteArray())
            fo.close()

            bird.imgLocation = imageFile.absolutePath
        } catch (_: Exception) {
        }

    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                uri = data!!.data
                binding.birdCreatImg.setImageURI(uri)
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