package com.example.orthoepy

import android.content.Context
import androidx.core.content.ContentProviderCompat.requireContext
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader


class Words {
    var newWords = mutableListOf<String>()
    private var words = mutableListOf(
        "бралА",
        "вернА",
        "вероисповЕдание",
        "взялА",
        "взялАсь",
        "включЁн",
        "включЁнный",
        "влилАсь",
        "добелА",
        "добралА",
        "добралАсь",
        "довезЁнный",
        "дОверху",
        "договорЁнность",
        "дождалАсь"
    )
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

    public fun getVowels():List<String>{
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

}