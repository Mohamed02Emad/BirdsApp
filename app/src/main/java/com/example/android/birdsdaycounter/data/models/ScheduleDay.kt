package com.example.android.birdsdaycounter.data.models

import com.example.android.birdsdaycounter.data.models.Food

data class ScheduleDay (
    val day : String,
    var dayFood : ArrayList<Food>
        )