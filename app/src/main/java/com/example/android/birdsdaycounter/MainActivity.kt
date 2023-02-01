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
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.example.android.birdsdaycounter.databinding.ActivityMainBinding
import com.example.android.birdsdaycounter.globalUse.MyApp.Companion.allBirds
import com.example.android.birdsdaycounter.presentation.allBirdsFragment.AllBirdsFragment
import com.example.android.birdsdaycounter.presentation.multiBirdsFragment.MultiBirdFragment
import com.example.android.birdsdaycounter.presentation.scheduleFragment.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    // lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var bottomNavigationView: BottomNavigationView

    private val allBirds = AllBirdsFragment.newInstance()
    private val home = HomeFragment.newInstance()
    private val multiBirds = MultiBirdFragment.newInstance()


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
        context = this
        setStatusBarGradiant(this)
        setContentView(view)
        setMeowNavigation()
        requestNotificationPermission()
    }

    private fun setMeowNavigation() {
        val bottomNavigation = findViewById<MeowBottomNavigation>(R.id.bottom_nav)


        bottomNavigation.add(MeowBottomNavigation.Model(1, R.drawable.bird))
        bottomNavigation.add(MeowBottomNavigation.Model(2, R.drawable.schedule_icon))
        bottomNavigation.add(MeowBottomNavigation.Model(3, R.drawable.heart))


        supportFragmentManager.beginTransaction().apply {
            add(R.id.frame_layout, allBirds,"allBirds").hide(allBirds)
            add(R.id.frame_layout, home, "home")
            add(R.id.frame_layout, multiBirds, "multiBirds").hide(multiBirds)
        }.commit()


        bottomNavigation.setOnClickMenuListener {
            when (it.id) {
                1 -> setFragment(allBirdsState = true, homeState = false, multiBirdsState = false)
                2 -> setFragment(allBirdsState = false, homeState = true, multiBirdsState = false)
                3 -> setFragment(allBirdsState = false, homeState = false, multiBirdsState = true)
            }
        }

        bottomNavigation.show(2)
    }

    fun setFragment(allBirdsState:Boolean,homeState:Boolean,multiBirdsState:Boolean) {
        if (allBirdsState){
            supportFragmentManager.beginTransaction()
                .show(allBirds)
                .hide(home)
                .hide(multiBirds)
                .commit()
        }else if (homeState){
            supportFragmentManager.beginTransaction()
                .hide(allBirds)
                .show(home)
                .hide(multiBirds)
                .commit()
        }else if (multiBirdsState){
            supportFragmentManager.beginTransaction()
                .hide(allBirds)
                .hide(home)
                .show(multiBirds)
                .commit()
        }
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


    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pushNotificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        lateinit var binding: ActivityMainBinding

        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        fun hideBottomNav(hide: Boolean) {
            if (hide) {
                binding.bottomNav.visibility = View.GONE
                setMargins(binding.frameLayout, 0, 0, 0, 0)

            } else {
                binding.bottomNav.visibility = View.VISIBLE
                setMargins(binding.frameLayout, 0, 0, 0, 120)
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
