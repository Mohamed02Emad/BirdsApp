package com.example.android.birdsdaycounter.singleBirds.classesMVVM

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.birdsdaycounter.singleBirds.models.SingleBirdCollection

class SingleBirdViewModel : ViewModel() {
    private var _collectionsLiveData = MutableLiveData<ArrayList<SingleBirdCollection>>()
    val collectionsLiveData : LiveData<ArrayList<SingleBirdCollection>> = _collectionsLiveData

    fun addNewCollection() {
        TODO("Not yet implemented")
    }

}