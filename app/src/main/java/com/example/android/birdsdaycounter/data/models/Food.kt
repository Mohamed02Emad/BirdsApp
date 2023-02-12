package com.example.android.birdsdaycounter.data.models

data class Food(
    var foodName: String,
    var foodAmount: Float,
    var foodImg: Int,
    var foodIsDone : Boolean,
    var foodNote : String?
    )