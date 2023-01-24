package com.example.android.birdsdaycounter.presentation

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.example.android.birdsdaycounter.data.models.Bird
import com.example.android.birdsdaycounter.databinding.ActivityAddBirdBinding
import com.example.android.birdsdaycounter.globalUse.MyApp
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import kotlin.math.log

private const val TAG = "AddBirdActivity"
class AddBirdActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBirdBinding
    var uri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBirdBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.setFinishOnTouchOutside(false)
        setOnClicks()
    }

    private fun setOnClicks() {
        binding.cancelButton.setOnClickListener {
            finish()
        }

        binding.addBirdCamera.setOnClickListener {
            Toast.makeText(this, "ToDo", Toast.LENGTH_SHORT).show()
            val i = Intent().apply {
                setType("image/*")
                setAction(Intent.ACTION_GET_CONTENT)
            }
            resultLauncher.launch(i)
        }

        binding.saveButton.setOnClickListener {
            val name = binding.birdNameET.text.toString()
            val age = binding.birdAgeET.text.toString()
            var id = binding.myRadioGroup.checkedRadioButtonId
            val gender = findViewById<RadioButton>(id).text.toString()
            val imgBitmap = binding.birdCreatImg.drawable.toBitmap()


            val bytes = ByteArrayOutputStream()
            imgBitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes)

            val bird = Bird(age, name, gender,null)
            saveBird(bird,bytes)
            finish()
        }
    }

    private fun saveBird(bird: Bird,byte:ByteArrayOutputStream) {

        // creating parent file
        val imgLocation = filesDir.absolutePath + File.separator
        val myAppDir = File(imgLocation)
        if (!myAppDir.exists()) myAppDir.mkdir()

        //creating child file
        val fileName = "${System.currentTimeMillis()}.png"
        val ImageFile = File(myAppDir,fileName)
        if (!ImageFile.exists()) ImageFile.createNewFile()

        try{
            
            val fo = FileOutputStream(ImageFile)
            fo.write(byte.toByteArray())
            fo.close()

            bird.imgLocation= ImageFile.absolutePath
            Log.i(TAG, "saveBird: ")
        }catch (E:Exception){
            Log.i(TAG, "saveBird: ${E.message}")

        }


        MyApp.addToAllBirdsArrayList(bird)
    }

    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                uri = data!!.data
                binding.birdCreatImg.setImageURI(uri)

            }
        }
}