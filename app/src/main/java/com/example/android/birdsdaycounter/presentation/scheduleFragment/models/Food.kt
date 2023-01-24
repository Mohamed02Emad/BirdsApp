package com.example.android.birdsdaycounter.presentation.scheduleFragment.models

data class Food(
    var foodName: String,
    var foodAmount: Float,
    var foodImg: Int,
    var foodIsDone : Boolean,
    var foodNote : String?
    )