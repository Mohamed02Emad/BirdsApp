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


class AllBirdsViewModel : ViewModel() {

    // should contain name and arrayList of Birds
    private var _listToDelete = MutableLiveData<ArrayList<Bird>?>()
    val listToDelete: LiveData<ArrayList<Bird>?> = _listToDelete

    private var _BirdsLiveData = MutableLiveData<ArrayList<Bird>?>()
    val BirdsLiveData: LiveData<ArrayList<Bird>?> = _BirdsLiveData

    var isReadyToShow = MutableLiveData<Boolean>(false)
    var newBirdWasAdded = MutableLiveData<Boolean>(false)
    var isSelectToDelete = MutableLiveData<Boolean>(false)


    private val repository: BirdsRepository

    init {
        _BirdsLiveData.value = ArrayList()
        _listToDelete.value = ArrayList()

        val dao = AllBirdsDataBaseClass.getInstance(MyApp.appContext).singleDao()
        repository = BirdsRepository(dao)

        viewModelScope.launch {
            _BirdsLiveData.value = getAllDB()
            isReadyToShow.value = true
        }

    }


    fun insertDB(bird: Bird) {
        insertBirdToDB(bird)
        viewModelScope.launch {
            _BirdsLiveData.value = getAllDB()
            isReadyToShow.value = true
        }

    }

    fun insertBirdToDB(bird: Bird) =
        viewModelScope.launch(Dispatchers.IO) { repository.insert(bird) }

    //
//    fun updateDB(bird: Bird) =
//        viewModelScope.launch(Dispatchers.IO) { repository.update(bird) }
//
    private suspend fun deleteDB(bird: Bird) =
        withContext(Dispatchers.IO) { repository.delete(bird) }
//
//    fun deleteByIdDB(id: Int) =
//        viewModelScope.launch(Dispatchers.IO) { repository.deleteById(id) }

    suspend fun clearDB() = withContext(Dispatchers.IO) { repository.deleteAll() }

    private suspend fun getAllDB(): ArrayList<Bird> = withContext(Dispatchers.IO) {
        repository.getAll() as ArrayList<Bird>
    }


    fun birdListSize() = _BirdsLiveData.value!!.size

    fun clearSelectedToBeDeleted() {
        _listToDelete.value!!.clear()
        isSelectToDelete.value = false
        _BirdsLiveData.value!!.forEach { it.isSelected = false }
    }

    fun checkToInsertToSelectedToBeDeleted(bird: Bird): Boolean {
        //if bird exists remove it from list and return false to deSelect it
        //else add it and return true
        if (_listToDelete.value!!.any { it.id == bird.id }) {
           _listToDelete.value!!.remove(bird)
            return false
        } else {
            _listToDelete.value!!.add(bird)
            Log.d("mohamed", "addToSelectedToBeDeleted:  " + _listToDelete.value!!.size)
            return true
        }
    }

    fun deleteSelected() {
        viewModelScope.launch(Dispatchers.IO) {
            _listToDelete.value!!.forEach {
                try {
                    deleteDB(it)
                } catch (E: Exception) {
                    Log.d("mohamed", "deleteSelected: isnot available")
                }
            }
            clearSelectedToBeDeleted()
        }
    }


    fun createFakeBuird(): Bird = Bird(birdListSize().toString(), "momo", "male", null)
}