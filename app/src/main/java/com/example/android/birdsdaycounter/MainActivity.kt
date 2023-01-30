package com.example.android.birdsdaycounter

import android.annotation.TargetApi
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.example.android.birdsdaycounter.databinding.ActivityMainBinding
import com.example.android.birdsdaycounter.presentation.allBirdsFragment.AllBirdsFragment
import com.example.android.birdsdaycounter.presentation.multiBirdsFragment.MultiBirdFragment
import com.example.android.birdsdaycounter.presentation.scheduleFragment.HomeFragment
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
        setStatusBarGradiant(this)
        setContentView(view)
        setMeowNavigation()
        //setupNavigation()
        requestNotificationPermission()
    }

    private fun setMeowNavigation() {
        val bottomNavigation = findViewById<MeowBottomNavigation>(R.id.bottom_nav)

      //  bottomNavigation.typeface = Typeface.createFromAsset(assets, "fonts/SourceSansPro-Regular.ttf")

        bottomNavigation.add(MeowBottomNavigation.Model(1, R.drawable.bird))
        bottomNavigation.add(MeowBottomNavigation.Model(2, R.drawable.schedule_icon))
        bottomNavigation.add(MeowBottomNavigation.Model(3, R.drawable.eye_icon))

        bottomNavigation.setOnClickMenuListener {
            when (it.id) {
                1 -> setFragment(AllBirdsFragment.newInstance())
                2 -> setFragment(HomeFragment.newInstance())
                3 -> setFragment(MultiBirdFragment.newInstance())
            }
        }

        bottomNavigation.show(2)
    }


    fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment, "main activity")
            .commit()

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun setStatusBarGradiant(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = activity.window
            val background = ContextCompat.getDrawable(activity, R.drawable.main_gradient_theme)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

            //   window.statusBarColor = ContextCompat.getColor(activity,android.R.color.transparent)
            window.navigationBarColor =
                ContextCompat.getColor(activity, android.R.color.transparent)

            //   window.setBackgroundDrawable(background)
        }
    }

    private fun setupNavigation() {
//        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
//
//        bottomNavigationView = findViewById<BottomNavigationView?>(R.id.bottom_nav)
//        NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment.navController)

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