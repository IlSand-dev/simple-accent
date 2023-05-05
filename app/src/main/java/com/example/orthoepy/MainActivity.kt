package com.example.orthoepy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {


    private var bottomNavigationView:BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val dictionaryFragment = DictionaryFragment()
        val teachingFragment = TeachingFragment()
        val profileFragment = ProfileFragment()
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentPlace, dictionaryFragment)
                .commit()
        }
        bottomNavigationView?.setOnNavigationItemSelectedListener{
            when(it.itemId){
                R.id.dictionary -> selectFragment(dictionaryFragment)
                R.id.teach -> selectFragment(teachingFragment)
                R.id.profile -> selectFragment(profileFragment)
            }
            true
        }
    }

    private fun selectFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentPlace, fragment)
                .commit()
        }
    }

}