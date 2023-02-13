package com.example.android.birdsdaycounter.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android.birdsdaycounter.R
import com.example.android.birdsdaycounter.globalUse.MyApp
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "Birds")
data class Bird(
   var age: String?,
   var name: String?,
   var gender: String?,
   var imgLocation: String? = null
):Parcelable{
   @PrimaryKey(autoGenerate = true) var id: Int = 0
   var note : String = ""
   var isPinned : Boolean = false

   var stateCode = -1   // -1 single

   var isSelected = false

   fun calculateAge():String{
      return ""
   }

}