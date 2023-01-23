package com.example.android.birdsdaycounter.allBirds.classesMVVM

import com.example.android.birdsdaycounter.allBirds.models.Bird
import com.example.android.birdsdaycounter.globalUse.MyApp
import com.example.android.birdsdaycounter.globalUse.allBirdsRoom.AllBirdsDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.withContext

class AllBirdsRepository(
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