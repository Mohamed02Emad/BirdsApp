package com.example.android.birdsdaycounter.allBirdsFragment.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "Collection")
 class Collection(
    var collectionName: String,
    var birdsList: ArrayList<Bird>?
    ){
    @PrimaryKey(autoGenerate = true) var id: Int = 0

}