package com.example.android.birdsdaycounter.globalUse

import android.app.Application
import android.content.Context
import com.example.android.birdsdaycounter.data.models.Bird
import com.example.android.birdsdaycounter.data.source.allBirdsRoom.AllBirdsDao
import com.example.android.birdsdaycounter.data.source.allBirdsRoom.AllBirdsDataBaseClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        allBirds = ArrayList()
        appContext = applicationContext
        customInit()

        // creating parent file

    }


    companion object {
        lateinit var appContext: Context
        lateinit var allBirds: ArrayList<Bird>
        lateinit var myDao : AllBirdsDao




        fun customInit() {
            myDao = AllBirdsDataBaseClass.getInstance(appContext).singleDao()
            GlobalScope.launch {
              allBirds.addAll(getAll())
            }
        }
        suspend fun getAll(): List<Bird> = withContext(Dispatchers.IO){
            myDao.getAll()
        }
        fun addToAllBirdsArrayList(bird: Bird){
           allBirds.add(bird)
            GlobalScope.launch {
                insert(bird)
            }
        }

        suspend fun insert(bird: Bird) {
            myDao.insertAll(bird)
        }
    }
}

