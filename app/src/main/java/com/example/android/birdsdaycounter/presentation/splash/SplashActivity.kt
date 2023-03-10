package com.example.android.birdsdaycounter.presentation.splash

import android.animation.Animator
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.example.android.birdsdaycounter.MainActivity
import com.example.android.birdsdaycounter.R

class SplashActivity : AppCompatActivity() {

    private lateinit var lottieAnimationView: LottieAnimationView
    lateinit var i: Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarGradiant(this)
        setContentView(R.layout.activity_splash)
        i = Intent(this, MainActivity::class.java)
        lottieAnimationView = findViewById(R.id.animationView)
        //setAnimation()
        whenAnimationEnd()
    }

    private fun whenAnimationEnd() {
        lottieAnimationView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator) {

            }

            override fun onAnimationEnd(p0: Animator) {
                startActivity(i)
                this@SplashActivity.finish()
            }

            override fun onAnimationCancel(p0: Animator) {

            }

            override fun onAnimationRepeat(p0: Animator) {

            }

        })
    }

    private fun showToast(string: String) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
    }

    private fun setAnimation() {

        when ((1..3).shuffled().last()) {
            1 -> lottieAnimationView.setAnimation(R.raw.blue_bird)
            2 -> lottieAnimationView.setAnimation(R.raw.yellow_bird)
            3 -> lottieAnimationView.setAnimation(R.raw.red_bird)
        }
    }

    private fun setStatusBarGradiant(activity: Activity) {
        val window: Window = activity.window
        val background = ContextCompat.getDrawable(activity, R.drawable.main_gradient_theme)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        //   window.statusBarColor = ContextCompat.getColor(activity,android.R.color.transparent)
        window.navigationBarColor =
            ContextCompat.getColor(activity, android.R.color.transparent)
        //   window.setBackgroundDrawable(background)

    }
}