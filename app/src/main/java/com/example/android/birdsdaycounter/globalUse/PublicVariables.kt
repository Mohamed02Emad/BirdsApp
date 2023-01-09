package com.example.android.birdsdaycounter.globalUse

import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("StaticFieldLeak")
object PublicVariables {
    private lateinit var context: Context
    fun setConctext(con: Context) {
        context = con
    }

    fun getContext(): Context = context

}