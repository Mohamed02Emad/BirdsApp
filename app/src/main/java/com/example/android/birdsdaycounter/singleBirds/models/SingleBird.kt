package com.example.android.birdsdaycounter.singleBirds.models

data class SingleBird(
    val image: Int?,
    val id: Int,
    val age : Int?,
    val name : String?,
    val color : String?,

    //if user wants to count days for something
    val startDate : String?,
    val daysFromStartDate : Int?,
    var countDays : Boolean,  // set default to false

    val note:String?,


)