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


class AllBirdsViewModel : ViewModel() {

    // should contain name and arrayList of Birds
    private var _collectionsLiveData = MutableLiveData<ArrayList<Bird>>()
    val collectionsLiveData: LiveData<ArrayList<Bird>?> = _collectionsLiveData

    var isReadyToShow = MutableLiveData<Boolean>()
    var newBirdWasAdded = MutableLiveData<Boolean>()

    private val repository: BirdsRepository

    init {
        isReadyToShow.value = false
        newBirdWasAdded.value = false
        _collectionsLiveData.value = ArrayList()
        val dao = AllBirdsDataBaseClass.getInstance(MyApp.appContext).singleDao()
        repository = BirdsRepository(dao)
        viewModelScope.launch {
            getAllDB()
            isReadyToShow.value = true
        }
    }

    fun resetArrayList() {
        getAllDB()
    }

    fun insertDB(bird: Bird) =
        viewModelScope.launch(Dispatchers.IO) { repository.insert(bird) }
//
//    fun updateDB(bird: Bird) =
//        viewModelScope.launch(Dispatchers.IO) { repository.update(bird) }
//
//    fun deleteDB(bird: Bird) =
//        viewModelScope.launch(Dispatchers.IO) { repository.delete(bird) }
//
//    fun deleteByIdDB(id: Int) =
//        viewModelScope.launch(Dispatchers.IO) { repository.deleteById(id) }

    fun clearDB() =
        viewModelScope.launch(Dispatchers.IO) { repository.deleteAll() }

    fun getAllDB(){
        viewModelScope.launch(Dispatchers.IO) {
            _collectionsLiveData.value = repository.getAll() as ArrayList<Bird>
        }
    }

    fun birdListSize() = _collectionsLiveData.value!!.size

    fun createFakeBuird(): Bird = Bird(birdListSize().toString(), "momo", "male", null)
}