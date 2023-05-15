package com.example.orthoepy

import android.content.Context
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader


class Words {
    var newWords = mutableListOf<String>()
    private val vowels = listOf(
        "а",
        "о",
        "у",
        "ы",
        "э",
        "е",
        "ё",
        "и",
        "ю",
        "я"
    )

    fun getVowels():List<String>{
        return vowels
    }

    fun getWords(context: Context):MutableList<String>{
        var oneWord:String?
        val inputStream: InputStream = context.resources.openRawResource(R.raw.dict)
        val reader = BufferedReader(InputStreamReader(inputStream))
        var eachLine : String? = reader.readLine()
        try{
            while(eachLine != null){
                oneWord = eachLine.trimEnd()
                eachLine = reader.readLine()
                newWords.add(oneWord)
            }
        }catch (_:Exception){

        }
        return newWords
    }

    fun getRule(ruleType:Int, context: Context):String{
        var s:String = ""
        var inputStream: InputStream? = null
        when(ruleType){
            1 -> inputStream = context.resources.openRawResource(R.raw.noun_rule)
            2 -> inputStream = context.resources.openRawResource(R.raw.verbs_rule)
            3 -> inputStream = context.resources.openRawResource(R.raw.adjective_rule)
            4 -> inputStream = context.resources.openRawResource(R.raw.participles_rule)
            5 -> inputStream = context.resources.openRawResource(R.raw.adverbs_rule)
            6 -> inputStream = context.resources.openRawResource(R.raw.adverbbbs_rule)
        }
        val reader = BufferedReader(InputStreamReader(inputStream))
        var eachLine : String? = reader.readLine()
        try{
            while(eachLine != null){
                s += eachLine.trimEnd()
                eachLine = reader.readLine()
                s += "\n"
            }
        }catch (_:Exception){

        }
        return s
    }

}