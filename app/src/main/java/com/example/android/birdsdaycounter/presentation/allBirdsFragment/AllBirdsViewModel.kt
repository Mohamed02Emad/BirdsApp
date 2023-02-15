package com.example.android.birdsdaycounter.presentation.allBirdsFragment

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

    private var _listToDelete = MutableLiveData<ArrayList<Pair<Bird,Int>>?>()
    val listToDelete: LiveData<ArrayList<Pair<Bird,Int>>?> = _listToDelete

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

    fun checkIfBirdIsSelected(bird: Bird,pos: Int): Boolean {
        //if bird exists remove it from list and return false to deSelect it
        //else add it and return true
        return if (_listToDelete.value!!.any { it.first.id == bird.id }) {
            _listToDelete.value!!.remove(bird to pos)
            false
        } else {
            _listToDelete.value!!.add(bird to pos)
            true
        }
    }


    suspend fun deleteSelected() {
            sortListToDelete()
            listToDelete.value!!.forEach { deleteDB(it.first) }
            birdsLiveData.value!!.clear()
           birdsLiveData.value!!.addAll(getAllDB())
           clearSelectedToBeDeleted()
    }


    suspend fun clearSelectedToBeDeleted() {
        _listToDelete.value!!.clear()
        withContext(Dispatchers.Main){ isSelectToDelete.value = false }
        _birdsLiveData.value!!.forEach { it.isSelected = false }
    }

    internal suspend fun insertDB(bird: Bird) = withContext(Dispatchers.IO) { repository.insert(bird) }

    private suspend fun deleteDB(bird: Bird) =
        withContext(Dispatchers.IO) {
            repository.deleteById(bird.id)
            deleteImage(bird.imgLocation)
        }

    private fun deleteImage(location: String?) {
        try {
            val file = File(location!!)
            file.delete()
        } catch (_: Exception) { }
    }

    private suspend fun getAllDB(): ArrayList<Bird> = withContext(Dispatchers.IO) { repository.getAll() as ArrayList<Bird> }

    suspend fun getDataFromRoom() {
        _birdsLiveData.value!!.clear()
        _birdsLiveData.value!!.addAll(getAllDB())
    }

    fun markTheBird(markBird: Boolean, pos: Int) {
        _birdsLiveData.value!![pos].isSelected = markBird
    }

    private fun sortListToDelete() { _listToDelete.value!!.sortByDescending { it.second } }

}