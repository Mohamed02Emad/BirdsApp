package com.example.android.birdsdaycounter.globalUse

import android.app.Application
import android.content.Context
import com.example.android.birdsdaycounter.allBirds.models.Bird
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        allBirds = ArrayList()
        appContext = applicationContext
    }


    companion object {
        lateinit var appContext: Context
        lateinit var allBirds: ArrayList<Bird>

        init {
            GlobalScope.launch {
                //get Data From Room to allBirds
            }
        }

    }
}

