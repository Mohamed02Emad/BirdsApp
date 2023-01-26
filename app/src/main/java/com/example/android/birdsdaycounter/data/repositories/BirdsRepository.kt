package com.example.android.birdsdaycounter.data.repositories

import com.example.android.birdsdaycounter.data.models.Bird
import com.example.android.birdsdaycounter.data.source.allBirdsRoom.AllBirdsDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BirdsRepository(
    private val myDao: AllBirdsDao
) {

    suspend fun getAll(): List<Bird> = withContext(Dispatchers.IO){
        myDao.getAll()
    }

    suspend fun insert(bird: Bird) {
        myDao.insertAll(bird)
    }

    suspend fun delete(bird: Bird) {
        myDao.delete(bird)
    }

    suspend fun deleteAll() {
        myDao.deleteAll()
    }

    suspend fun update(bird: Bird) {
        myDao.update(bird)
    }

    suspend fun deleteById(id: Int) = myDao.deleteById(id)
}