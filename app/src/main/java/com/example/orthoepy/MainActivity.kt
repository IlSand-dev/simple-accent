package com.example.orthoepy

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sharedPref : SharedPreferences = this.getSharedPreferences("Test", Context.MODE_PRIVATE)
        val isUserNew = sharedPref.getBoolean("NEW_USER", false)
        if(!isUserNew){
            val registerFragment = RegisterFragment()
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.main_frame, registerFragment)
                    .commit()
            }
        }
        else{
            val mainFragment = MainFragment()
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.main_frame, mainFragment)
                    .commit()
            }
        }
    }

}