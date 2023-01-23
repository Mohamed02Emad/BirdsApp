package com.example.android.birdsdaycounter.allBirdsFragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import com.example.android.birdsdaycounter.databinding.ActivityAddBirdBinding

class AddBirdActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBirdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBirdBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.setFinishOnTouchOutside(false)
        setOnClicks()
    }

    private fun setOnClicks() {
        binding.cancelButton.setOnClickListener {
            finish()
        }

        binding.addBirdCamera.setOnClickListener {
            Toast.makeText(this,"ToDo",Toast.LENGTH_SHORT).show()
        }

        binding.saveButton.setOnClickListener {
            Toast.makeText(this,"ToDo",Toast.LENGTH_SHORT).show()
            val name = binding.birdNameET.text
            val age = binding.birdAgeET.text
            var id = binding.myRadioGroup.checkedRadioButtonId
            val gender = findViewById<RadioButton>(id).text


        }
    }


}