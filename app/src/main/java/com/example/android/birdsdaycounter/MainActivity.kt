package com.example.android.birdsdaycounter

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
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

   // lateinit var binding: ActivityMainBinding
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
        context=this
        setStatusBarGradiant(this)
        setContentView(view)
        setMeowNavigation()
        //setupNavigation()
        requestNotificationPermission()
    }

    private fun setMeowNavigation() {
        val bottomNavigation = findViewById<MeowBottomNavigation>(R.id.bottom_nav)


        bottomNavigation.add(MeowBottomNavigation.Model(1, R.drawable.bird))
        bottomNavigation.add(MeowBottomNavigation.Model(2, R.drawable.schedule_icon))
        bottomNavigation.add(MeowBottomNavigation.Model(3, R.drawable.heart))

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

    companion object {

        @SuppressLint("StaticFieldLeak")
        lateinit var binding : ActivityMainBinding
        lateinit var context: Context

           fun hideBottomNav(hide: Boolean) {
            if (hide) {
                binding.bottomNav.visibility = View.GONE
                setMargins(binding.frameLayout,0,0,0,0)

            } else {
                binding.bottomNav.visibility = View.VISIBLE
                setMargins(binding.frameLayout,0,0,0,120)
            }
        }

        private fun setMargins(view: View, left: Int, top: Int, right: Int, bottom: Int) {
            if (view.layoutParams is MarginLayoutParams) {
                val p = view.layoutParams as MarginLayoutParams
                p.setMargins(left, top, right, bottom)
                view.requestLayout()
            }
        }
    }

}
