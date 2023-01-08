package com.example.android.birdsdaycounter.singleBirds.classesMVVM

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.birdsdaycounter.singleBirds.models.Bird
import com.example.android.birdsdaycounter.singleBirds.models.Collection

class SingleBirdViewModel : ViewModel() {

    // should contain name and arrayList of Birds
    private var _collectionsLiveData = MutableLiveData<ArrayList<Collection>?>()
    val collectionsLiveData: LiveData<ArrayList<Collection>?> = _collectionsLiveData

    init {
        _collectionsLiveData.value = ArrayList()
    }

    fun addNewCollection(string: String) {
        var arrayList:ArrayList<Bird> = ArrayList()
        _collectionsLiveData.value!!.add(Collection(string,arrayList))
    }

    fun collectionSize() = _collectionsLiveData.value!!.size
}