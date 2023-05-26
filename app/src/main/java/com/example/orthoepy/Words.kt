package com.example.orthoepy

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader


class Words {

    private lateinit var sharedPref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

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

    fun getVowels(): List<String> {
        return vowels
    }

    fun getWords(context: Context): MutableList<String> {
        val newWords = mutableListOf<String>()
        var oneWord: String?
        val inputStream: InputStream = context.resources.openRawResource(R.raw.dict)
        val reader = BufferedReader(InputStreamReader(inputStream))
        var eachLine: String? = reader.readLine()
        try {
            while (eachLine != null) {
                oneWord = eachLine.trimEnd()
                eachLine = reader.readLine()
                newWords.add(oneWord)
            }
        } catch (_: Exception) {

        }
        return newWords
    }

    fun saveState(words: MutableList<Pair<String, Int>>, context: Context) {
        val jsonObject = JSONObject()
        words.forEach { pair -> jsonObject.put(pair.first, pair.second) }
        sharedPref = context.getSharedPreferences("Test", Context.MODE_PRIVATE)
        editor = sharedPref.edit()
        editor.putString("SAVED_STATE", jsonObject.toString())
        editor.apply()
    }

    fun newGetRandomWords(context: Context): MutableList<Pair<String, Int>> {
        val words = mutableListOf<Pair<String, Int>>()

        val getStateString =
            context.getSharedPreferences("Test", Context.MODE_PRIVATE).getString("SAVED_STATE", "")
        if (getStateString != "") {
            val jsonObject = JSONObject(getStateString)
            jsonObject.keys().forEach { word ->
                words.add(Pair(word, jsonObject.getInt(word)))

            }
        }
        else{
            getWords(context).shuffled().forEach { word -> words.add(Pair(word, 0)) }
        }
        return words
    }

    fun getRandomWords(context: Context): MutableList<MutableList<String>> {
        return mutableListOf(
            getWords(context).shuffled().toMutableList(),
            mutableListOf(),
            mutableListOf(),
            mutableListOf(),
            mutableListOf()
        )
    }

    fun getRule(ruleType: Int, context: Context): String {
        var s: String = ""
        var inputStream: InputStream? = null
        when (ruleType) {
            1 -> inputStream = context.resources.openRawResource(R.raw.noun_rule)
            2 -> inputStream = context.resources.openRawResource(R.raw.verbs_rule)
            3 -> inputStream = context.resources.openRawResource(R.raw.adjective_rule)
            4 -> inputStream = context.resources.openRawResource(R.raw.participles_rule)
            5 -> inputStream = context.resources.openRawResource(R.raw.adverbs_rule)
            6 -> inputStream = context.resources.openRawResource(R.raw.adverbbbs_rule)
        }
        val reader = BufferedReader(InputStreamReader(inputStream))
        var eachLine: String? = reader.readLine()
        try {
            while (eachLine != null) {
                s += eachLine.trimEnd()
                eachLine = reader.readLine()
                s += "\n"
            }
        } catch (_: Exception) {

        }
        return s
    }

}