package com.example.android.birdsdaycounter.singleBirds.models

data class Bird(
    var image: Int?,
    var id: Int,
    var age : Int?,
    var name : String?,
    var color : String?,
    var gender : String?,

    //if user wants to count days for something
    var startDate : String?,
    var daysFromStartDate : Int?,
    var endDate:Int?,
    var countDays : Boolean,  // set default to false

    var note:String?,
)