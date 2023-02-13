package com.example.android.birdsdaycounter.data.repositories

import android.util.Log
import com.example.android.birdsdaycounter.data.models.Bird
import com.example.android.birdsdaycounter.data.source.allBirdsRoom.AllBirdsDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BirdsRepository(
    private val myDao: AllBirdsDao
) {

    suspend fun getAll(): List<Bird> = withContext(Dispatchers.IO){
        Log.d("mohamed", "getAll: ")
        myDao.getAll()
    }

    suspend fun insert(bird: Bird) {
        Log.d("mohamed", "insert: ")
        myDao.insertAll(bird)
    }

    suspend fun delete(bird: Bird) {
        Log.d("mohamed", "delete: ")
        myDao.delete(bird)
    }

    suspend fun deleteAll() {
        Log.d("mohamed", "deleteAll: ")
        myDao.deleteAll()
    }

    suspend fun update(bird: Bird) {
        Log.d("mohamed", "update: ")
        myDao.update(bird)
    }

    suspend fun deleteById(id: Int) = myDao.deleteById(id)
}