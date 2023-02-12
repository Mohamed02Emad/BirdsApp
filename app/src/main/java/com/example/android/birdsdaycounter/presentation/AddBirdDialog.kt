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
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.viewModelScope
import com.example.android.birdsdaycounter.R
import com.example.android.birdsdaycounter.data.models.Bird
import com.example.android.birdsdaycounter.globalUse.MyApp
import com.example.android.birdsdaycounter.presentation.allBirdsFragment.AllBirdsViewModel
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


class AddBirdDialog(private val viewModel: AllBirdsViewModel) : DialogFragment() {
    private var uri: Uri? = null
    private lateinit var cancelBTN : Button
    private lateinit var saveBTN : Button
    private lateinit var cameraBTN:Button
    private lateinit var nameET: EditText
    private lateinit var ageET:EditText
    private lateinit var img: ImageView
    private lateinit var radioGroup: RadioGroup

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_bird_dialog,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViews(view)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().setFinishOnTouchOutside(false)
    }
    private fun setViews(view: View) {
        cancelBTN= view.findViewById(R.id.cancel_button)
        saveBTN= view.findViewById(R.id.save_button)
        cameraBTN= view.findViewById(R.id.add_bird_camera)
        nameET = view.findViewById(R.id.bird_name_ET)
        ageET=view.findViewById(R.id.bird_age_ET)
        img=view.findViewById(R.id.bird_creat_img)
        radioGroup=view.findViewById(R.id.myRadioGroup)
        setOnClicks(view)

    }

    private fun setOnClicks(view: View) {

       cancelBTN.setOnClickListener {
           this.dismiss()
        }

        cameraBTN.setOnClickListener {

            val i = Intent().apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
            }
            resultLauncher.launch(i)
        }

       saveBTN.setOnClickListener {
            val name = nameET.text.toString()
            val age = ageET.text.toString()
            val id = radioGroup.checkedRadioButtonId
            val gender = view.findViewById<RadioButton>(id).text.toString()


            val imgBitmap = img.drawable.toBitmap()
            val bytes = ByteArrayOutputStream()
            imgBitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes)



            val bird = Bird(age, name, gender,null)

            saveBird(bird,bytes)
       }
    }

    private fun saveBird(bird: Bird,byte:ByteArrayOutputStream) {

        viewModel.viewModelScope.launch {
            saveBirdImage(bird, byte)
            MyApp.addToAllBirdsArrayList(bird)
            viewModel.newBirdWasAdded.value = true
        }
        this.dismiss()
    }

    private fun saveBirdImage(bird: Bird, byte: ByteArrayOutputStream) {
            val imgLocation = MyApp.appContext.filesDir.absolutePath + File.separator
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
                // Log.i(TAG, "saveBird: ")
            } catch (E: Exception) {
                //  Log.i(TAG, "saveBird: ${E.message}")
            }

    }


    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                uri = data!!.data
                img.setImageURI(uri)

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