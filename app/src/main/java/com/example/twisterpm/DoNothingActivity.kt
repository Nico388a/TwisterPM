package com.example.twisterpm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class DoNothingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_do_nothing)
    }



    fun doNothingClick(view: View) {
        finish()
    }
}