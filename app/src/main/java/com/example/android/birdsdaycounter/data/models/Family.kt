package com.example.android.birdsdaycounter.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "Family")
data class Family (
    var familyName : String?,
    var male : Int?,
    var female : Int?
        ) : Parcelable {
    @PrimaryKey(autoGenerate = true) var id : Int = 0
    var numberOfEggs = 0
    var numberOfKids = 0
}