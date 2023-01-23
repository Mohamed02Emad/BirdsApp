package com.example.android.birdsdaycounter.allBirds.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Birds")
 class Bird(
    var image: Int?,
    var age : Int?,
    var name : String?,
    var gender : String?
){
@PrimaryKey(autoGenerate = true) var id: Int = 0
}