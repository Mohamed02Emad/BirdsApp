package com.example.android.birdsdaycounter.presentation.allBirdsFragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.birdsdaycounter.data.models.Bird
import com.example.android.birdsdaycounter.data.repositories.BirdsRepository
import com.example.android.birdsdaycounter.data.source.allBirdsRoom.AllBirdsDataBaseClass
import com.example.android.birdsdaycounter.globalUse.MyApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


class AllBirdsViewModel : ViewModel() {

    private var _listToDelete = MutableLiveData<ArrayList<Bird>?>()
    val listToDelete: LiveData<ArrayList<Bird>?> = _listToDelete

    private var _birdsLiveData = MutableLiveData<ArrayList<Bird>?>()
    val birdsLiveData: LiveData<ArrayList<Bird>?> = _birdsLiveData

    var isReadyToShow = MutableLiveData(false)
    var newBirdWasAdded = MutableLiveData(false)
    var isSelectToDelete = MutableLiveData(false)

    private val repository: BirdsRepository

    init {
        _birdsLiveData.value = ArrayList()
        _listToDelete.value = ArrayList()

        val dao = AllBirdsDataBaseClass.getInstance(MyApp.appContext).singleDao()
        repository = BirdsRepository(dao)

        viewModelScope.launch {
            _birdsLiveData.value = getAllDB()
            isReadyToShow.value = true
        }
    }


    fun birdListSize() = _birdsLiveData.value!!.size

    fun checkIfBirdIsSelected(bird: Bird): Boolean {
        //if bird exists remove it from list and return false to deSelect it
        //else add it and return true
        return if (_listToDelete.value!!.any { it.id == bird.id }) {
            _listToDelete.value!!.remove(bird)
            false
        } else {
            _listToDelete.value!!.add(bird)
            true
        }
    }

    fun deleteSelected() {
        viewModelScope.launch(Dispatchers.IO) {
            _listToDelete.value!!.forEach {
                _birdsLiveData.value!!.remove(it)
                deleteDB(it)
            }
            clearSelectedToBeDeleted()
        }
    }

    fun clearSelectedToBeDeleted() {
        viewModelScope.launch(Dispatchers.Main) {
            _listToDelete.value!!.clear()
            isSelectToDelete.value = false
            _birdsLiveData.value!!.forEach { it.isSelected = false }
        }
    }


    internal suspend fun insertDB(bird: Bird) = withContext(Dispatchers.IO) {
        repository.insert(bird)
    }

    private suspend fun deleteDB(bird: Bird) =
        withContext(Dispatchers.IO) { repository.delete(bird)
        deleteImage(bird.imgLocation)
        }

    private fun deleteImage(location : String?) {
        try {
            val file = File(location)
            file.delete()
        } catch (_: Exception) {
        }
    }

    private suspend fun getAllDB(): ArrayList<Bird> =
        withContext(Dispatchers.IO) { repository.getAll() as ArrayList<Bird> }

    suspend fun getDataFromRoom() {
            _birdsLiveData.value!!.clear()
            _birdsLiveData.value!!.addAll(getAllDB())
            Log.d(MyApp.TAG, "getDataFromRoom: " + _birdsLiveData.value!!.size)


    }
}