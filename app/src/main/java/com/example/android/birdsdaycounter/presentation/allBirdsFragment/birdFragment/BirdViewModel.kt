package com.example.android.birdsdaycounter.presentation.allBirdsFragment.birdFragment

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.birdsdaycounter.data.models.Bird
import com.example.android.birdsdaycounter.globalUse.MyApp
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class BirdViewModel : ViewModel() {
    var leaveControlFlag = false
    var uri  = MutableLiveData<Uri?>()
    private var _birdLiveData = MutableLiveData<Bird>()
    val bird: LiveData<Bird> = _birdLiveData

    private var _firstTimeChangLiveData = MutableLiveData(true)
    val firstTimeChange: LiveData<Boolean> = _firstTimeChangLiveData

    fun initBird(bird: Bird){
        _birdLiveData.value=bird
        uri.value = null
    }

    fun changePic(img: Drawable?) {

        if (uri!=null) {
            val imgLocation = MyApp.appContext.filesDir.absolutePath + File.separator
            val myAppDir = File(imgLocation)
            if (!myAppDir.exists()) myAppDir.mkdir()

            val fileName = "${System.currentTimeMillis()}.png"
            val ImageFile = File(myAppDir, fileName)
            if (!ImageFile.exists()) ImageFile.createNewFile()

            val imgBitmap = img!!.toBitmap()
            val byte = ByteArrayOutputStream()
            imgBitmap.compress(Bitmap.CompressFormat.PNG, 100, byte)

            try {
                val fo = FileOutputStream(ImageFile)
                fo.write(byte.toByteArray())
                fo.close()

                val fd = File(_birdLiveData.value!!.imgLocation.toString())
                fd.delete()

                _birdLiveData.value!!.imgLocation = ImageFile.absolutePath
                // Log.i(TAG, "saveBird: ")
            } catch (E: Exception) {
                //  Log.i(TAG, "saveBird: ${E.message}")
            }
        }
    }

    fun imageCheck(): Boolean =
        uri.value!=null

    fun setFirstTimeChange(b: Boolean) {
        _firstTimeChangLiveData.value=b
    }

}