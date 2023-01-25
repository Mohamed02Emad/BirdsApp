package com.example.android.birdsdaycounter.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Birds")
data class Bird(
   var age: String?,
   var name: String?,
   var gender: String?,
   var imgLocation: String? = null
){
   @PrimaryKey(autoGenerate = true) var id: Int = 0
}