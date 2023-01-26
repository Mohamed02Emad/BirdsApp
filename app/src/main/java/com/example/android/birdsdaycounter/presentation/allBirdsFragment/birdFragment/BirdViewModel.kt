package com.example.android.birdsdaycounter.presentation.allBirdsFragment.birdFragment

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.birdsdaycounter.data.models.Bird

class BirdViewModel : ViewModel() {
    var leaveControlFlag = false

    private var _birdLiveData = MutableLiveData<Bird>()
    val bird: LiveData<Bird> = _birdLiveData

    fun initBird(bird: Bird){
        _birdLiveData.value=bird
    }


}