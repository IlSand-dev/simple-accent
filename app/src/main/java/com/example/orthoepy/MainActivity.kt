package com.example.orthoepy

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader


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

    private fun readFile(){
        val words = Words()
        var oneWord:String?
        val inputStream: InputStream = this.resources.openRawResource(R.raw.dict)
        val reader = BufferedReader(InputStreamReader(inputStream))
        var eachLine : String? = reader.readLine()
        try{
            while(eachLine != null){
                oneWord = eachLine.trimEnd()
                eachLine = reader.readLine()
                words.addWord(oneWord)
            }
            println(words.getWords())
        }catch (_:Exception){

        }
    }

}