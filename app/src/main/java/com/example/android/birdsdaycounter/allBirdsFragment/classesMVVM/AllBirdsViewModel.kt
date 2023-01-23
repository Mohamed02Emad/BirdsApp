package com.example.android.birdsdaycounter.allBirdsFragment.classesMVVM

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.birdsdaycounter.R
import com.example.android.birdsdaycounter.allBirdsFragment.models.Bird
import com.example.android.birdsdaycounter.globalUse.MyApp
import com.example.android.birdsdaycounter.globalUse.allBirdsRoom.AllBirdsDataBaseClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AllBirdsViewModel : ViewModel() {

    // should contain name and arrayList of Birds
    private var _collectionsLiveData = MutableLiveData<ArrayList<Bird>>()
    val collectionsLiveData: LiveData<ArrayList<Bird>?> = _collectionsLiveData

    var isReadyToShow = MutableLiveData<Boolean>()

     val repository: AllBirdsRepository

    init {
        isReadyToShow.value=false
        _collectionsLiveData.value = ArrayList()
        val dao = AllBirdsDataBaseClass.getInstance(MyApp.appContext).singleDao()
        repository = AllBirdsRepository(dao)
        viewModelScope.launch {
            _collectionsLiveData.value = repository.getAll() as ArrayList<Bird>
            isReadyToShow.value=true
        }
    }

    fun insertDB(bird: Bird) =
        viewModelScope.launch(Dispatchers.IO) { repository.insert(bird) }

    fun updateDB(bird: Bird) =
        viewModelScope.launch(Dispatchers.IO) { repository.update(bird) }

    fun deleteDB(bird: Bird) =
        viewModelScope.launch(Dispatchers.IO) { repository.delete(bird) }

    fun deleteByIdDB(id: Int) =
        viewModelScope.launch(Dispatchers.IO) { repository.deleteById(id) }

    fun clearDB() =
        viewModelScope.launch(Dispatchers.IO) { repository.deleteAll() }


    fun addNewBird(bird: Bird) {
        _collectionsLiveData.value!!.add(bird)
           insertDB(bird)
    }

    fun birdListSize() = _collectionsLiveData.value!!.size


//    fun removeCollection(collection: Collection): Int {
//        var pos = _collectionsLiveData.value!!.indexOf(collection)
//        _collectionsLiveData.value!!.remove(collection)
//        //  deleteByIdDB(collection.id)
//        return pos
//    }


    fun createFakeBuird():Bird = Bird(R.drawable.bird,birdListSize(),"momo","male")

}