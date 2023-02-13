package com.example.android.birdsdaycounter.globalUse

import android.app.Application
import android.content.Context
import android.media.CamcorderProfile.getAll
import com.example.android.birdsdaycounter.data.models.Bird
import com.example.android.birdsdaycounter.data.models.Family
import com.example.android.birdsdaycounter.data.source.allBirdsRoom.AllBirdsDao
import com.example.android.birdsdaycounter.data.source.allBirdsRoom.AllBirdsDataBaseClass
import com.example.android.birdsdaycounter.data.source.familyRoom.FamilyDao
import com.example.android.birdsdaycounter.data.source.familyRoom.FamilyDataBaseClass
import kotlinx.coroutines.*

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext


    }


    companion object {
        lateinit var appContext: Context
    }
}

