package com.example.android.birdsdaycounter.splash

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView
import com.example.android.birdsdaycounter.MainActivity
import com.example.android.birdsdaycounter.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    lateinit var lottieAnimationView: LottieAnimationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val i: Intent = Intent(this, MainActivity::class.java)
        lottieAnimationView = findViewById(R.id.animationView)

        setAnimation()
        whenAnimationEnd()
        lifecycleScope.launch {
            delay(3000)
            startActivity(i)
            this@SplashActivity.finish()
        }
    }

    private fun whenAnimationEnd() {
        lottieAnimationView.addAnimatorListener (object : Animator.AnimatorListener{
            override fun onAnimationStart(p0: Animator) {
                //TODO("Not yet implemented")
            }

            override fun onAnimationEnd(p0: Animator) {
                setAnimation()
            }

            override fun onAnimationCancel(p0: Animator) {
               // TODO("Not yet implemented")
            }

            override fun onAnimationRepeat(p0: Animator) {
             //   TODO("Not yet implemented")
            }

        })
    }

    private fun setAnimation() {
        val random = (1..3).shuffled().last()

        when (random) {
            1 -> lottieAnimationView.setAnimation(R.raw.blue_bird)
            2 -> lottieAnimationView.setAnimation(R.raw.yellow_bird)
            3->  lottieAnimationView.setAnimation(R.raw.red_bird)

        }
    }

}