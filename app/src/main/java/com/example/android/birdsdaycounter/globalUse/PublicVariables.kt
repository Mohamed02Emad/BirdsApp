package com.example.android.birdsdaycounter.globalUse

import android.app.Application
import android.content.Context
import com.example.android.birdsdaycounter.data.models.Bird
import com.example.android.birdsdaycounter.data.models.Family
import com.example.android.birdsdaycounter.data.source.allBirdsRoom.AllBirdsDao
import com.example.android.birdsdaycounter.data.source.allBirdsRoom.AllBirdsDataBaseClass
import com.example.android.birdsdaycounter.data.source.familyRoom.FamilyDao
import com.example.android.birdsdaycounter.data.source.familyRoom.FamilyDataBaseClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        allBirds = ArrayList()
        familyArrayList = ArrayList()
        appContext = applicationContext
        customInit()

        // creating parent file

    }


    companion object {
        lateinit var appContext: Context
        lateinit var allBirds: ArrayList<Bird>
        lateinit var familyArrayList: ArrayList<Family>
        lateinit var myDao: AllBirdsDao
        lateinit var familyDao : FamilyDao


        fun customInit() {
            myDao = AllBirdsDataBaseClass.getInstance(appContext).singleDao()
            familyDao = FamilyDataBaseClass.getInstance(appContext).familyDao()
            GlobalScope.launch {
                allBirds.addAll(getAll())
            }
            setFamilies()
        }

        private fun setFamilies() {
            GlobalScope.launch {
                familyArrayList.addAll(getAllFamily())
            }
        }

        suspend fun getAllFamily(): List<Family> = withContext(Dispatchers.IO) {
            familyDao.getAll()
        }

        suspend fun updateBirdFamily(family:Family) = withContext(Dispatchers.IO) {
            familyDao.update(family)
        }

        suspend fun getAll(): List<Bird> = withContext(Dispatchers.IO) {
            myDao.getAll()
        }

        suspend fun updateBird(bird: Bird) = withContext(Dispatchers.IO) {
            myDao.update(bird)
        }

        fun addToAllBirdsArrayList(bird: Bird) {
            allBirds.add(bird)
            GlobalScope.launch {
                myDao.insertAll(bird)
            }
        }
    }

}

