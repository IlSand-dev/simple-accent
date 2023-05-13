package com.example.orthoepy

class Words {
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

    public fun getWords():List<String>{
        return words
    }
    public fun getVowels():List<String>{
        return vowels;
    }

}