package com.example.android.birdsdaycounter.globalUse.allBirdsRoom

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [com.example.android.birdsdaycounter.allBirdsFragment.models.Bird::class], version = 1)

abstract class AllBirdsDataBaseClass : RoomDatabase() {

    abstract fun singleDao(): AllBirdsDao

    companion object {
        private var instancee: AllBirdsDataBaseClass? = null

        private const val DB_NAME = "AllBirdsDB"

        fun getInstance(context: Context): AllBirdsDataBaseClass {

            return instancee ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context,
                    AllBirdsDataBaseClass::class.java,
                    DB_NAME
                ).build()
                instancee = instance
                instance

        }


        }


    }



}