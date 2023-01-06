package com.example.android.birdsdaycounter

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.android.birdsdaycounter.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var bottomNavigationView: BottomNavigationView

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
        setContentView(view)

        setupNavigation()
        requestNotificationPermission()
    }

    private fun setupNavigation() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        bottomNavigationView = findViewById(R.id.bottom_nav)
        NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment.navController)
    }

    fun bottomBarNavigationVisibility(isVisible: Boolean) {
        bottomNavigationView.isVisible = isVisible
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pushNotificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }

}


/*
create collections
each collection contains some birds

each bird has {
image of them
number of eggs
each egg laying date and expected hatching date
when the expected date comes notification should arrive

if the user marked an egg as a little bird
new waiting period should start and a notification should arrive when the bird is ready
to leave the nest

else if the egg didn't hatch you can simply delete it


you can add new little bird or new egg at any time
you can also remove them at any time
}

you can add a single bird to caculate it's age day after another
this bird can have image and number or just number



you can make a schedule for your food and medicine

use bottom navigation to navigate between schedule (Home) , collections and single birds
 */