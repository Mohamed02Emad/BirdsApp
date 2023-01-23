package com.example.android.birdsdaycounter.splash

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.example.android.birdsdaycounter.MainActivity
import com.example.android.birdsdaycounter.R

class SplashActivity : AppCompatActivity() {

    lateinit var lottieAnimationView: LottieAnimationView
    lateinit var i: Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
         i = Intent(this, MainActivity::class.java)
        lottieAnimationView = findViewById(R.id.animationView)
        setAnimation()
        whenAnimationEnd()
    }

    private fun whenAnimationEnd() {
        lottieAnimationView.addAnimatorListener (object : Animator.AnimatorListener{
            override fun onAnimationStart(p0: Animator) {
                //TODO("Not yet implemented")
            }

            override fun onAnimationEnd(p0: Animator) {
                startActivity(i)
                this@SplashActivity.finish()            }

            override fun onAnimationCancel(p0: Animator) {
               // TODO("Not yet implemented")
            }

            override fun onAnimationRepeat(p0: Animator) {
             //   TODO("Not yet implemented")
            }

        })
    }

    private fun showToast(string: String){
        Toast.makeText(this,string,Toast.LENGTH_SHORT).show()
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