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
import com.example.android.birdsdaycounter.R
import com.example.android.birdsdaycounter.data.models.Bird
import com.example.android.birdsdaycounter.globalUse.MyApp
import com.example.android.birdsdaycounter.presentation.allBirdsFragment.AllBirdsViewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class AddBirdDialog(val viewModel: AllBirdsViewModel) : DialogFragment() {
    var uri: Uri? = null
    lateinit var cancelBTN : Button
    lateinit var saveBTN : Button
    lateinit var cameraBTN:ImageView
    lateinit var nameET:EditText
    lateinit var ageET:EditText
    lateinit var img:ImageView
    lateinit var radioGroup:RadioGroup

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

            // todo: bassam
            val i = Intent().apply {
                setType("image/*")
                setAction(Intent.ACTION_GET_CONTENT)
            }
            resultLauncher.launch(i)
        }

       saveBTN.setOnClickListener {
            val name = nameET.text.toString()
           // todo: bassaam -> remember to change age method
            val age = ageET.text.toString()
            var id = radioGroup.checkedRadioButtonId

           // todo: bassam -> remember to change choice color
            val gender = view.findViewById<RadioButton>(id).text.toString()


            val imgBitmap = img.drawable.toBitmap()
            val bytes = ByteArrayOutputStream()
            imgBitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes)



            val bird = Bird(age, name, gender,null)

           //todo: suzan make sure that user have entered data before saving
            saveBird(bird,bytes)
       }
    }

    private fun saveBird(bird: Bird,byte:ByteArrayOutputStream) {

        val imgLocation = MyApp.appContext.filesDir.absolutePath + File.separator
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
           // Log.i(TAG, "saveBird: ")
        }catch (E:Exception){
            //  Log.i(TAG, "saveBird: ${E.message}")

        }
        MyApp.addToAllBirdsArrayList(bird)
       viewModel.newBirdWasAdded.value=true
        this.dismiss()
    }



    var resultLauncher =
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