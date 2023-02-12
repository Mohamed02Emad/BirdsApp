package com.example.android.birdsdaycounter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.view.Window
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.example.android.birdsdaycounter.databinding.ActivityMainBinding
import com.example.android.birdsdaycounter.presentation.allBirdsFragment.AllBirdsFragment
import com.example.android.birdsdaycounter.presentation.multiBirdsFragment.MultiBirdFragment
import com.example.android.birdsdaycounter.presentation.scheduleFragment.HomeFragment


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val pushNotificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            //Toast.makeText(this, "granted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setStatusBarGradiant()
        setContentView(view)
        requestNotificationPermission()
    }


    private fun setStatusBarGradiant() {
            val window: Window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.navigationBarColor = ContextCompat.getColor(this, android.R.color.transparent)
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pushNotificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }


}
