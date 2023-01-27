package com.example.android.birdsdaycounter.data.source.familyRoom

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.android.birdsdaycounter.data.models.Family

@Database(entities = [Family::class], version = 1)

abstract class FamilyDataBaseClass : RoomDatabase() {

    abstract fun familyDao(): FamilyDao

    companion object {
        private var instancee: FamilyDataBaseClass? = null

        private const val DB_NAME = "FamilyDB"

        fun getInstance(context: Context): FamilyDataBaseClass {

            return instancee ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context,
                    FamilyDataBaseClass::class.java,
                    DB_NAME
                ).build()
                instancee = instance
                instance
            }
        }
    }
}